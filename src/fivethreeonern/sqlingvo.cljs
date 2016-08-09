(ns fivethreeonern.sqlingvo
  (:require [mount.core :refer-macros [defstate]]
            [sqlingvo.db :as db]))

(defstate db :start (db/sqlite))
