package de.spacekookie.netsync

import java.net.SocketTimeoutException

import scala.collection.mutable.Queue

import com.esotericsoftware.kryonet.Client

class NetClient {
  val client: Client = new Client()
  val cmdBuffer: Queue[() => Unit] = new Queue[() => Unit]()

  /**
   * Create a new netsync client that will run on a separate thread
   * to keep registered objects in sync with a server. The target to
   * sync object changes to needs to be provided via
   *
   */
  def NetClient() {

    val self = this

    /** The NetClient runs asynchronously most of the time */
    new Thread(new Runnable {
      def run() {
        self.synchronized {

          /** Run all queued commands */
          for (cmd <- cmdBuffer)
            cmd()
        }
      }
    }).start()

  }

  /**
   * Connect to a server target via an address and port provided. You
   * can also provide an optional callback handle to be executed with
   * a connect status either when the connection is established or
   * when the connection attempts have timed out or permanently failed.
   *
   * A function doesn't have to be provided but if it is, should be able
   * to handle both success and failure cases for further processing.
   *
   * @param target: The address of the server target
   * @param port: The port the server listens to
   * @param callback: An optional callback function
   */
  def connect(target: String, port: Int, callback: Option[Int => Unit]) = {
    this.synchronized {
      cmdBuffer.enqueue { () =>

        var ret = 0
        try {
          client.connect(5000, target, port, port)
        } catch {
          case t: SocketTimeoutException => ret = 1
        }

        /** Only execute callback if it exists */
        callback match {

          /** This is an ugly hack because Java Threads don't know what functions are :) */
          case Some(callback) => new Thread(new Runnable {
            def run() {
              callback(ret)
            }
          }).start()

          case none =>
          /** Do absolutely nothing **/
        }
      }

      /** Wake up the wait() next door */
      this.notify()
    }
  }

  /**
   * Disconnect from the currently connected server. Will do nothing if no
   * server connection is currently active. An optional callback function can
   * be provided to execute code after a successful disconnect or when the
   * disconnect step has permanently failed.
   */
  def disconnect(callback: (Int) => Option[Unit]) = {

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