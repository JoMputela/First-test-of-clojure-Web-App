(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

;;we’ll create the definition for our database connection which is a map
;;containing the class for the JDBC driver, the protocol, and the name of the database file used by SQLite.

(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})

;;Now that we have a database connection declared above, let’s write a function below to
;;create the table for storing the guest messages.

(defn create-guestbook-table []
  (sql/with-connection
    db
    (sql/create-table
      :guestbook
      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
      [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
      [:name "TEXT"]
      [:message "TEXT"])
    (sql/do-commands "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

(defn read-guests []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM guestbook ORDER BY timestamp DESC"]
      (doall res))))

;;create a function below to save new messages to our guestbook table. This function will call insert-values and pass it the name and the message to be stored as parameters.
;;to read and write messages

(defn save-message [name message]
  (sql/with-connection
    db
    (sql/insert-values
     :guestbook
     [:name :message :timestamp]
     [name message (new java.util.Date)])))






