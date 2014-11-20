(ns scratchbase.app
  (:require [ring.adapter.jetty as ring]
            [scratchbase.database :as db])
  (:gen-class))

(defn -main []
  (schema/migrate))
