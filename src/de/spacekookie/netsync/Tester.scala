package de.spacekookie.netsync

import de.spacekookie.netsync.ClientCodes._

object Tester {

  def main(args: Array[String]) = {

    println("Trying to test the shit we've just made...")

    /** Create a new client with 5 seconds timeout */
    val client: NetClient = new NetClient(5000)

    /** Try to connect to a server */
    client.connect("localhost", 3334, Option((x: ClientCodes) => {
      println("This function can do some error handling on the return value!")
      println(s"Connect return was $x")
    }))

    println("Foo?")

    while (true) {
      // DON'T LET THIS THREAD DIE!
    }
  }
}