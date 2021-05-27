(ns need-for-speed.core
    (:require
      [goog.string :as gstring]
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Models

(defonce cars
  [{:name "A" :consumption 3.0}
   {:name "B" :consumption 3.5}
   {:name "C" :consumption 4.0}])

;; -------------------------
;; Views

(defn car-checkbox [{:keys [name consumption]}]
  [:label.block {:key name}
   [:input {:type "radio" :name "car"}]
   (gstring/format "Car %s (%.1f litres/100km at 1km/h)" name consumption)])

(defn number-input [label name default-value]
  [:label.block
   [:input.border.border-gray-400 {:type "number" :name name :defaultValue default-value}]
   label])

(defn home-page []
  [:div
   [:h1.text-2xl "Need for Speed"]
   [:form
    [:fieldset
     [:legend "Choose your car"]
     (map car-checkbox cars)]
    [number-input "Distance" "distance" 100]
    [number-input "Speed X" "speed-x" 60]
    [number-input "Speed Y" "speed-y" 120]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
