(ns scratchbase.database.migration
  (:require [clojure.java.jdbc :as sql]))

;; Use (System/getenv "DATABASE_URL")?
(def psql-db {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//127.0.0.1:5432/scratchbase"
              :user "scratchbase"
              :password "scratchbase"
              })

(def table-prefix "sb_table")

(defn uuid
  "Generate a unique UUID for attaching to a table"
  [] (str (java.util.UUID/randomUUID)))

(defn new-table-name
  "Create a table name from the table prefix and a uuid."
  [] (keyword (str table-prefix "_" (uuid))))

(defn quote-value
  "Returns a quoted value. Example: :test => 'test'"
  [value] (sql/quoted \' (name value)))

(defn migrated?
  "Check to see if the passed table has been created."
  [table]
  (-> (sql/query psql-db
                 [(str "select count(*) from information_schema.tables "
                       "where table_name="
                       (quote-value table))])
      first :count pos?))

(defn migrate
  "If the table does not already exist, create it."
  [table]
  (when (not (migrated? table))
    (print (str "Create table: " (name table) "... ")) (flush)
    (sql/db-do-commands psql-db
                        (sql/create-table-ddl
                         (keyword table)
                         [:id :serial "PRIMARY KEY"]
                         [:body :varchar "NOT NULL"]
                         [:created_at :timestamp
                          "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
    (println " done.")))

(defn get-table-names
  "Get the table names of table names that start with the passed table value."
  ([] (get-table-names table-prefix))
  ([table]
   (->> (sql/query psql-db
                   [(str "select table_name from information_schema.tables "
                         "where table_name like "
                         (quote-value (str (name table) "%")))])
        (map :table_name))))

(defn drop-table
  "Drop the passed table."
  [table]
  (print (str "Dropping table: " (name table) " ...")) (flush)
  (sql/db-do-commands psql-db
                      (str "DROP TABLE IF EXISTS " (name table)))
  (println " done."))
