(ns fivethreeonern.android.core
  (:require [mount.core :as mount]
            [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [fivethreeonern.migrations :refer [migrate]]
            [fivethreeonern.sqlite :refer [query]]
            [fivethreeonern.handlers]
            [fivethreeonern.subs]))

(def react-native (js/require "react-native"))
(def sqllite (js/require "react-native-sqlite-storage"))

(def app-registry (.-AppRegistry react-native))
(def text (r/adapt-react-class (.-Text react-native)))
(def view (r/adapt-react-class (.-View react-native)))
(def image (r/adapt-react-class (.-Image react-native)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight react-native)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert react-native) title))

(defn run-migrations []
  (query "CREATE TABLE IF NOT EXISTS migrations (id INTEGER);" migrate))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style    {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press run-migrations}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "MIGRATE ME"]]])))

(defn init []
  (mount/start)
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "fivethreeonern" #(r/reactify-component app-root)))
