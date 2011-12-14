package controllers

import play.api._

import play.api.mvc._
import play.api.data._
import views._
import scala.xml._

import _root_.java.net.{URLDecoder, URLEncoder}

object Application extends Controller {
  val xmlloader = XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
  val searchForm = Form("channel" -> requiredText)

  val channelOptions = List[(String, String)](
    "2" -> "2 - SiriusXM Hits 1",
    "3" -> "3 - 20 on 20",
    "4" -> "4 - '40s on 4",
    "5" -> "5 - '50s on 5",
    "6" -> "6 - '60s on 6",
    "7" -> "7 - '70s on 7",
    "8" -> "8 - '80s on 8",
    "9" -> "9 - '90s on 9",
    "10" -> "10 - The Pulse",
    "15" -> "15 - BBC Radio 1",
    "16" -> "16 - The Blend",
    "17" -> "17 - SiriusXM Love",
    "18" -> "18 - SiriusXM Limited Engagements",
    "19" -> "19 - Elvis Radio",
    "20" -> "20 - E Street Radio",
    "21" -> "21 - Underground Garage",
    "22" -> "22 - Pearl Jam Radio",
    "23" -> "23 - Grateful Dead Channel",
    "24" -> "24 - Radio Margaritaville",
    "25" -> "25 - Classic Rewind",
    "26" -> "26 - Classic Vinyl",
    "27" -> "27 - Deep Tracks",
    "28" -> "28 - The Spectrum",
    "29" -> "29 - Jam_ON",
    "30" -> "30 - The Loft",
    "31" -> "31 - The Coffee House",
    "32" -> "32 - The Bridge",
    "33" -> "33 - 1st Wave",
    "34" -> "34 - Lithium",
    "35" -> "35 - SiriusXM U",
    "36" -> "36 - Alt Nation",
    "37" -> "37 - Octane",
    "38" -> "38 - Boneyard",
    "39" -> "39 - Hair Nation",
    "40" -> "40 - Liquid Metal",
    "41" -> "41 - Faction",
    "42" -> "42 - The Joint",
    "44" -> "44 - Hip-Hop Nation",
    "45" -> "45 - Shade 45",
    "46" -> "46 - Backspin",
    "47" -> "47 - The Heat",
    "48" -> "48 - Heart & Soul",
    "49" -> "49 - Soul Town",
    "51" -> "51 - BPM",
    "52" -> "52 - Electric Area",
    "53" -> "53 - SiriusXM Chill",
    "56" -> "56 - Willie's Roadhouse",
    "58" -> "58 - Prime Country",
    "59" -> "59 - The Highway",
    "60" -> "60 - Outlaw Country",
    "61" -> "61 - Bluegrass Junction",
    "63" -> "63 - The Message",
    "64" -> "64 - Praise",
    "66" -> "66 - Watercolors",
    "67" -> "67 - Real Jazz",
    "68" -> "68 - Spa",
    "69" -> "69 - Escape",
    "70" -> "70 - BB King's Bluesville",
    "71" -> "71 - Siriusly Sinatra",
    "72" -> "72 - On Broadway",
    "74" -> "74 - Met Opera Radio",
    "75" -> "75 - SiriusXM Pops",
    "76" -> "76 - Symphony Hall",
    "78" -> "78 - Kids Place Live",
    "79" -> "79 - Radio Disney",
    "80" -> "80 - SiriusXM Book Radio",
    "81" -> "81 - Doctor Radio",
    "82" -> "82 - RadioClassics",
    "84" -> "84 - ESPN Radio",
    "85" -> "85 - ESPN Xtra",
    "86" -> "86 - Mad Dog Radio",
    "88" -> "88 - SiriusXM NFL Radio",
    "90" -> "90 - SiriusXM NASCAR Radio",
    "91" -> "91 - SiriusXM College Sports Nation",
    "92" -> "92 - Sports Play-by-Play",
    "93" -> "93 - Sports Play-by-Play",
    "94" -> "94 - Sports Play-by-Play",
    "96" -> "96 - Laugh USA",
    "97" -> "97 - Blue Collar Radio",
    "98" -> "98 - The Foxxhole",
    "99" -> "99 - Raw Dog Comedy",
    "100" -> "100 - Howard 100",
    "101" -> "101 - Howard 101",
    "102" -> "102 - Playboy Radio",
    "103" -> "103 - Spice Radio",
    "104" -> "104 - SiriusXM Stars Too",
    "106" -> "106 - Road Dog Trucking",
    "107" -> "107 - SiriusXM Stars",
    "108" -> "108 - OutQ",
    "109" -> "109 - Cosmo Radio",
    "110" -> "110 - Martha Stewart Living Radio",
    "112" -> "112 - CNBC",
    "113" -> "113 - Bloomberg Radio",
    "114" -> "114 - FOX News Channel",
    "115" -> "115 - CNN",
    "116" -> "116 - HLN",
    "117" -> "117 - MSNBC",
    "118" -> "118 - BBC World Service",
    "120" -> "120 - World Radio Network",
    "122" -> "122 - NPR Now",
    "123" -> "123 - NPR Talk",
    "124" -> "124 - POTUS Politics",
    "125" -> "125 - SiriusXM Patriot",
    "126" -> "126 - FOX News Talk",
    "127" -> "127 - SiriusXM Left",
    "128" -> "128 - The Power",
    "129" -> "129 - The Catholic Channel",
    "130" -> "130 - EWTN Global Catholic Network",
    "131" -> "131 - Family Talk",
    "132" -> "132 - Boston/Philadelphia/Pittsburgh",
    "133" -> "133 - New York",
    "134" -> "134 - Washington, DC/Baltimore/Atlanta",
    "135" -> "135 - Chicago/Detroit",
    "136" -> "136 - Miami/Orlando/Tampa-St. Pete",
    "137" -> "137 - Dallas-Ft. Worth/Houston/Phoenix",
    "138" -> "138 - St. Louis/Minneapolis/Las Vegas",
    "139" -> "139 - San Francisco/Seattle/San Diego",
    "140" -> "140 - Los Angeles",
    "148" -> "148 - CNN En Espanol",
    "149" -> "149 - ESPN Deportes",
    "150" -> "150 - Caliente",
    "151" -> "151 - Iceberg",
    "152" -> "152 - CBC Radio 3",
    "154" -> "154 - L'Oasis Francophone",
    "155" -> "155 - Latitude Franco",
    "156" -> "156 - Sports Extra",
    "157" -> "157 - Sports Express",
    "158" -> "158 - The Score",
    "159" -> "159 - CBC Radio One",
    "160" -> "160 - Premiere Plus",
    "161" -> "161 - Bandeapart",
    "162" -> "162 - The Weather Network",
    "204" -> "204 - Oprah Radio",
    "205" -> "205 - SiriusXM Public Radio",
    "206" -> "206 - the ViRUS",
    "207" -> "207 - NHL Home Ice",
    "208" -> "208 - PGA TOUR Network",
    "209" -> "209 - MLB Network Radio",
    "210" -> "210 - SiriusXM Fantasy Sports Radio",
    "211" -> "211 - Sports Play-by-Play",
    "212" -> "212 - Sports Play-by-Play",
    "213" -> "213 - Sports Play-by-Play",
    "214" -> "214 - Sports Play-by-Play",
    "215" -> "215 - Sports Play-by-Play",
    "216" -> "216 - Sports Play-by-Play",
    "217" -> "217 - Sports Play-by-Play",
    "218" -> "218 - Sports Play-by-Play",
    "219" -> "219 - Sports Play-by-Play",
    "220" -> "220 - Sports Play-by-Play")

  val channelMap = Map(channelOptions.map { s => (s._1, s._2) }: _*)

  def index = Action {
    Ok(views.html.index(searchForm, channelOptions))
  }

  def search(channel: String, page: Int) = Action {
    val realPage = if (page < 1) 1 else page
    doSearch(channel, page) { res =>
      val filledSearchForm = searchForm.fill(channel)
      Ok(views.html.list(page, res, filledSearchForm, channelOptions))
    }
  }

  def searchPosted = Action { implicit request =>
    searchForm.bindFromRequest.fold(
      formWithErrors => BadRequest("oops"),
      {
        case (channel) =>
          Redirect(routes.Application.search(channel, 1))
      })
  }

  class SongInfo(vals: Array[String]) {
    val artist: String = vals(0)
    val song: String = vals(1)
    val date: String = vals(2)
    val time: String = vals(3)
    def toXml() = {
      <song>
        <artist>{ artist }</artist>
        <name>{ song }</name>
        <date>{ date }</date>
        <time>{ time }</time>
      </song>
    }
  }

  def searchAsXml(channel: String, page: Int) = Action { implicit request =>
    doSearch(channel, page) { res =>
      val songs: Seq[SongInfo] = res map { new SongInfo(_) }
      println(songs)
      val xml = <dogstarradio>
                  <channel><number>{ channel }</number><name>{ channelMap(channel) }</name></channel>
                  <songs>
                    { songs.map(_.toXml()) }
                  </songs>
                </dogstarradio>
      Ok(xml)
    }
  }

  def findSong(artist: String, song: String) = Action {
    val encodedArtist = URLEncoder.encode(artist, "UTF-8")
    val encodedSong = URLEncoder.encode(song, "UTF-8")
    val url = "http://www.dogstarradio.com/xspf_player/xspf_musicbrainz.php?artist=%s&title=%s".format(encodedArtist, encodedSong)
    AsyncResult {
      WS.url(url).get().map { s =>
        val xml = xmlloader.loadString(s.getResponseBody())
        println(xml)
        Ok(views.html.lookup(xml.toString))
      }
    }
  }

  private def doSearch(channel: String, page: Int)(f: Seq[Array[String]] => play.api.mvc.Result): play.api.mvc.Result = {
    // dogstarradio uses a zero-index for pagination, but we prefer a one-based index since it's more
    // user friendly
    val adjustedPage = page - 1
    val url = "http://www.dogstarradio.com/search_playlist.php?artist=&title=&channel=%s&month=&date=&shour=&sampm=&stz=&ehour=&eampm=&page=%d".format(channel, adjustedPage)
    AsyncResult {
      WS.url(url).get().map { s =>
        val searchResults = getSearchResults(s)
        f(searchResults)
      }
    }
  }

  private def getSearchResults(s: com.ning.http.client.Response): Seq[Array[String]] = {
    val xml = xmlloader.loadString(s.getResponseBody())
    val tableHtml = xml \\ "table"
    val trs = tableHtml \ "tr"
    val startIdx = trs.findIndexOf { n => val att: String = n.child(0).attribute("class").getOrElse("").toString; att == "channel" }
    val filteredTrs = trs.slice(startIdx + 1, trs.length - 4)
    val out: Seq[Array[String]] = filteredTrs.map { x =>
      Array(x.child(1).text, x.child(2).text, x.child(3).text, x.child(4).text)
    }
    out
  }
  
  // -- Javascript routing

  def javascriptRoutes = Action {
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Application.findSong
      )
    ).as("text/javascript") 
  }
}