(ns fivethreeonern.migrations
  (:require [fivethreeonern.sqlingvo :refer [db]]
            [sqlingvo.core :refer [select from insert values delete where]]))

(def migrations [{:id 1
                  :up (fn [])
                  :down (fn [])}])

(defn last-migration-id
  []
  (-> db
      (select [:id] (from :migrations))
      ;; TODO: create transaction thing
      last
      :id))

(defn insert-migration
  [id]
  (-> db
      (insert :migrations [:id] (values [id]))
      ;; TODO: create transaction thing
      ))

(defn delete-migration
  [id]
  (-> db
      (delete :migrations (where '(= :id id)))
      ;; TODO: create transaction thing
      ))

(defn migrate
  []
  (let [to-migrate-count (or (last-migration-id) 0)]
    (doseq [{:keys [id up]} (drop migrations to-migrate-count)]
      (up)
      (insert-migration id))))

(defn rollback
  ([id]
   (let [to-rollback-count (- (count migrations) id)]
     (when (> to-rollback-count 0)
       (doseq [{:keys [down id]} (take (reverse migrations) to-rollback-count)]
         (down)
         (delete-migration id)))))
  ([]
    (rollback 0)))