package models;

class SongInfo(vals: Seq[String]) {
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
  
case class Channel(val number:Int, val name:String) {
  def toXml() = {
    <channel>
      <number>{ number }</number>
      <name>{ name }</name>
    </channel>
  }
}