(ns need-for-speed.results-tests
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [need-for-speed.results :as results]))

(deftest fuel-consumption-multiplier
  (testing "1.009^0 for 1 km/h"
    (is (= 1 (results/fuel-consumption-multiplier 1))))
  (testing "1.009^1 for 2 km/h"
    (is (= 1.009 (results/fuel-consumption-multiplier 2))))
  (testing "1.009^2 for 3 km/h"
    (is (= 1.018081 (results/fuel-consumption-multiplier 3)))))

;; TODO: I'm getting TypeError: "cljs.test is undefined" 😿
(run-tests)
