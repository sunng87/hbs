(ns hbs.test-web-server
  (:require [ring.adapter.jetty :refer [run-jetty]])
  (:import [java.net ServerSocket]
           [java.io IOException]))

(defn- open-port [port]
  (try (ServerSocket. port)
       (catch IOException e nil)))

(defn find-free-port
  ([] (find-free-port 49152 65535))
  ([min-port max-port]
   (let [ports (range min-port max-port)]
     (loop [try-port (first ports)
            rest-ports (rest ports)]
       (if-let [socket (open-port try-port)]
         (do
           (.close socket)
           try-port)
         (if (empty? rest-ports)
           (throw (IOException.
                   (str "No ports available between " min-port " and " max-port)))
           (recur (first rest-ports)
                  (rest rest-ports))))))))

(defn- handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello {{name}}!"})

(defmacro with-test-webserver
  [port & body]
  `(let [server-thread# (Thread. #(run-jetty ~handler {:port ~port
                                                       :host "localhost"}))]
     (.start server-thread#)
     (Thread/sleep 1000)
     ~@body
     (.stop server-thread#)))
