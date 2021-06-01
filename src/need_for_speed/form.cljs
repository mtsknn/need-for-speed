(ns need-for-speed.form
  (:require
   [goog.string :as gstring]
   [need-for-speed.data :as data]
   [reagent.core :as r]))

(defonce state
  (r/atom {:car (-> data/cars first key)
           :distance 200
           :speed-x 80
           :speed-y 120}))

(defn- car-checkbox [[key {:keys [:consumption]}]]
  (let [set-car #(swap! state assoc :car %)]
    (fn []
      [:label.block
       [:input {:type "radio"
                :name "Car"
                :value key
                :checked (= key (@state :car))
                :on-change #(set-car key)}]
       (gstring/format "Car %s (%.1f liters/100 km at 1 km/h)" (name key) consumption)])))

(defn- car-checkboxes []
  [:fieldset
   [:legend "Choose your car"]
   (for [car data/cars]
     ^{:key (key car)} [car-checkbox car])])

(defn- number-input [label key]
  (let [set-input-value (fn [event]
                          (let [value (-> event .-target .-value int)]
                            (when (< 0 value 10000) ;; TODO: Replace with validation messages?
                              (swap! state assoc key value))))]
    (fn []
      [:label.block
       [:input.border.border-gray-400 {:type "number"
                                       :name label
                                       :value (@state key)
                                       :on-change #(set-input-value %)}]
       label])))

(defn view []
  [:form
   [car-checkboxes]
   [number-input "Distance (km)" :distance]
   [number-input "Speed X (km/h)" :speed-x]
   [number-input "Speed Y (km/h)" :speed-y]])
