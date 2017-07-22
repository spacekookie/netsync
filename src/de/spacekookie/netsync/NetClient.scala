package de.spacekookie.netsync

import com.esotericsoftware.kryonet.Client

class NetClient {
  var self: Option[Client] = null

  /**
   * Create a new netsync client that will run on a separate thread
   * to keep registered objects in sync with a server
   *
   * @param port: Specify the port to bind tcp/udp to
   */
  def NetClient(port: Int) {

  }

  /**
   * Register a new object and all of it's children that are required
   * in order to properly serialise it. This function is pretty slow
   * and shouldn't be called inside your standard run loop
   *
   * @param obj: An object you want to register
   */
  def register(obj: Unit) = {

  }
}