(ns need-for-speed.form
  (:require
   [goog.string :as gstring]
   [need-for-speed.data :as data]
   [reagent.core :as r]))

(defonce state
  (r/atom {:car (-> data/cars first key)
           :distance 220
           :speed-x 90
           :speed-y 110}))

(defn- car-checkbox [[key {:keys [:consumption]}]]
  (let [id (str "car-" (name key))
        set-car #(swap! state assoc :car %)]
    (fn []
      [:div.flex.items-center
       [:input {:type "radio"
                :name "car"
                :id id
                :value key
                :checked (= key (@state :car))
                :on-change #(set-car key)
                :class "h-4 w-4 text-indigo-600 border-gray-300 focus:(ring(indigo-500 offset-2))"}]
       [:label {:class "ml-3 block text(sm gray-700)"
                :for id}
        (gstring/format "Car %s (%.1f liters/100 km at 1 km/h)" (name key) consumption)]])))

(defn- car-checkboxes []
  [:fieldset.mt-6
   [:legend.text-base.font-medium.text-gray-900 "Choose your car"]
   [:div.mt-4.space-y-4
    (for [car data/cars]
      ^{:key (key car)} [car-checkbox car])]])

(defn- number-input [label key max]
  (let [set-value (fn [event]
                    (let [value (-> event .-target .-value int)]
                      (when (<= 1 value max)
                        (swap! state assoc key value))))]
    (fn []
      [:div
       [:label {:for key
                :class "block text(base gray-700) font-medium"}
        label]
       [:div.mt-1
        [:input {:type "number"
                 :min 1
                 :name key
                 :id key
                 :class ["block border-gray-300 rounded-md text-sm shadow-sm w-full"
                         "focus:(ring-indigo-500 border-indigo-500)"
                         "sm:w-1/2 md:w-1/3"]
                 :value (@state key)
                 :on-input #(set-value %)}]]])))

(defn view []
  [:form
   [car-checkboxes]
   [:div.mt-6.grid.grid-cols-1.gap-y-6
    [number-input "Distance (km)" :distance 100000]
    [number-input "Speed X (km/h)" :speed-x 500]
    [number-input "Speed Y (km/h)" :speed-y 500]]])
