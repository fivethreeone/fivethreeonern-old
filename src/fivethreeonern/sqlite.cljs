(ns fivethreeonern.sqlite
  (:require [mount.core :refer-macros [defstate]]))

(def node-module (js/require "react-native-sqlite-storage"))

(defstate sqlite
  :start (.openDatabase node-module #js {:name "531" :createFromLocation 1} #(js/console.log "sql ok") #(js/console.log "sql error")))

