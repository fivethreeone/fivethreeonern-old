(ns fivethreeonern.migrations
  (:require [fivethreeonern.sqlingvo :refer [db]]
            [fivethreeonern.sqlite :refer [query]]
            [sqlingvo.core :refer [select from insert values delete where order-by create-table]]))

(def migrations ["CREATE TABLE IF NOT EXISTS test_migrations (id INTEGER);"])

(declare run-migrations)

(defn last-migration-id
  []
  (-> @db
      (select [:id]
              (from :migrations)
              (order-by '(:id)))
      (query (comp :id last))))

(defn insert-migration
  [id rest-migrations]
  (-> @db
      (insert :migrations [:id] (values [id]))
      (query (fn [_]
               (if (empty? rest-migrations)
                 (prn "Migrations succeeded!")
                 (run-migrations rest-migrations))))))

(defn run-migrations [[[id migration] & rest-migrations]]
  (query migration (fn [_]
                     (insert-migration id rest-migrations))))

(defn migrate
  [_]
  (let [to-migrate-count (or (last-migration-id) 0)]
    (run-migrations (map-indexed vector (drop to-migrate-count migrations)))))