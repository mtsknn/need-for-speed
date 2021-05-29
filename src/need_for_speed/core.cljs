(ns need-for-speed.core
    (:require
      [goog.string :as gstring]
      [need-for-speed.form :as form]
      [need-for-speed.results :as results]
      [reagent.core :as r]
      [reagent.dom :as d]))

(defn app []
  [:div.font-mono.max-w-xl.mx-auto.p-6
   [:h1.text-2xl "Need for Speed"]
   [form/view]
   [results/view]])

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
