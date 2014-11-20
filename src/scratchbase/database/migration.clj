(ns scratchbase.database.migration
  (:require [clojure.java.jdbc :as sql]))

(def psql-db {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//127.0.0.1:5432/scratchbase"
              :user "scratchbase"
              :password "scratchbase"
              })

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/shouter"))

(defn migrated? []
  (-> (sql/query psql-db
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='test'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Create database structure...") (flush)
    (sql/db-do-commands psql-db
                        (sql/create-table-ddl
                         :test
                         [:id :serial "PRIMARY KEY"]
                         [:body :varchar "NOT NULL"]
                         [:created_at :timestamp
                          "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
    (println " done")))
