package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.cache._
import views._
import scala.xml._
import _root_.java.net.{ URLDecoder, URLEncoder }
import play.api.data.Forms._
import models._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import javax.inject.Inject
import play.api.Logger

class Application @Inject() (cache: CacheApi) extends Controller {
  val xmlloader = scala.xml.XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
  val searchForm = Form("channel" -> text)
  lazy val channelOptions = findChannels
  lazy val channelMap = Map(channelOptions.map { s => (s._1.toInt, s._2) }: _*)

  def index = Action {
    Ok(views.html.index(searchForm, channelOptions))
  }

  def search(channel: Int, page: Int) = Action { implicit request =>
    val realPage = if (page < 1) 1 else page
    doSearch(channel, page) { res =>
      val filledSearchForm = searchForm.fill(channel.toString)
      Ok(views.html.list(page, res, filledSearchForm, channelOptions))
    }
  }

  def searchPosted = Action { implicit request =>
    searchForm.bindFromRequest.fold(
      formWithErrors => BadRequest("oops"),
      {
        case (channel) =>
          Redirect(routes.Application.search(channel.toInt, 1))
      })
  }

  def playlistAsXml(channel: Int, page: Int) = Action { implicit request =>
    doSearch(channel, page) { res =>
      val songs: Seq[SongInfo] = res.map { new SongInfo(_) }
      val xml = <dogstarradio>
                  <channel><number>{ channel }</number><name>{ channelMap(channel) }</name></channel>
                  <songs>
                    { songs.map(_.toXml()) }
                  </songs>
                </dogstarradio>
      Ok(xml)
    }
  }

  def searchAsXml(channel: Int, page: Int) = Action { implicit request =>
    Redirect(routes.Application.playlistAsXml(channel, page))
  }

  def channelsAsXml() = Action { implicit request =>
    val channels: Iterable[Channel] = channelMap.keys.toList.sorted.map { key => new Channel(key, channelMap.getOrElse(key, "")) }
    val xml = <dogstarradio>
                <channels>
                  { channels.map(_.toXml()) }
                </channels>
              </dogstarradio>
    Ok(xml)
  }
  
  def getByAtt(e: Elem, nodeType: String, att: String, value: String): NodeSeq = {
    def filterAtribute(node: Node, att: String, value: String) = (node \ ("@" + att)).text == value
    e \\ nodeType filter { n => filterAtribute(n, att, value) }
  }
  
  def findChannels(): List[(String, String)] = {
    val url = "http://www.dogstarradio.com/search_playlist.php"
    val xml = xmlloader.loadString(utils.WebService.get(url))
    val selectChannels = getByAtt(xml, "select", "name", "channel")
    val channels = for (x <- (selectChannels \\ "option") if !x.attribute("value").isEmpty) yield (x.attribute("value").get.text, x.text)
    Logger.info(s"$channels")
    Logger.info("hello???")
    println("hi???????????")
    println(channels)
    channels.toList
  }

  // TODO: clean up the results to erase unused javascript and make the
  // formatting prettier. Make sure we keep the affiliate links for
  // Mr. Dogstar's Amazon links though.
  def findSong(artist: String, song: String) = Action {
    val encodedArtist = URLEncoder.encode(artist, "UTF-8")
    val encodedSong = URLEncoder.encode(song, "UTF-8")
    val url = "http://www.dogstarradio.com/xspf_player/xspf_musicbrainz.php?artist=%s&title=%s".format(encodedArtist, encodedSong)
    // AsyncResult doesn't work on Heroku
    /*AsyncResult {
      WS.url(url).get().map { s =>
        val xml = xmlloader.loadString(s.getResponseBody())
        println(xml)
      }
      Ok(views.html.lookup(xml.toString))
    }*/
    val s = utils.WebService.get(url)
    val xml = xmlloader.loadString(s)
    Ok(views.html.lookup(xml.toString))
  }

  private def doSearch(channel: Int, page: Int)(f: Seq[Seq[String]] => play.api.mvc.Result): play.api.mvc.Result = {
    // dogstarradio uses a zero-index for pagination, but we prefer a one-based index since it's more
    // user friendly
    val adjustedPage = page - 1
    val url = "http://www.dogstarradio.com/search_playlist.php?artist=&title=&channel=%s&month=&date=&shour=&sampm=&stz=&ehour=&eampm=&page=%d".format(channel, adjustedPage)
    // AsyncResult doesn't work on Heroku
    /*AsyncResult {
      WS.url(url).get().map { s =>
        val searchResults = getSearchResults(s.getResponseBody())
      }
      f(searchResults)
    }*/
    val searchResults = getSearchResults(utils.WebService.get(url))
    f(searchResults)
  }

  private def getSearchResults(s: String): Seq[Seq[String]] = {
    val xml = xmlloader.loadString(s)
    val tableHtml = xml \\ "table"
    val trs = (tableHtml \ "tr")
    val tds = (trs \ "td").filter(!_.attribute("channel").isDefined)
    trs.map(x => (x \ "td").slice(1, 5).map(_.text)).filter(_.length == 4).drop(1)
  }

  // -- Javascript routing

  def javascriptRoutes() = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Application.findSong)).as("text/javascript")
  }
}