package controllers
import scala.xml.XML

class Test {
  val xmlloader = XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
  def getSearchResults(s: String): Seq[Array[String]] = {
    val xml = xmlloader.loadString(s)
    val tableHtml = xml \\ "table"
    val trs = tableHtml \ "tr"
    val startIdx = trs.findIndexOf { n => val att: String = n.child(0).attribute("class").getOrElse("").toString; att == "channel" }
    val filteredTrs = trs.slice(startIdx + 1, trs.length - 4)
    val out: Seq[Array[String]] = filteredTrs.map { x =>
      Array(x.child(1).text, x.child(2).text, x.child(3).text, x.child(4).text)
    }
    out
  }
}

object Test {
  val tester = new Test()
  val url = "http://www.dogstarradio.com/search_playlist.php"
  val searchResults = tester.getSearchResults(utils.WebService.get(url))
  println(searchResults)
}