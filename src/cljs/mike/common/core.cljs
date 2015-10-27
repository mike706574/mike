(ns mike.common.core
  (:require [clojure.string :refer [blank? capitalize]]))

(enable-console-print!)

(def not-blank? (comp not blank?))

(def is-number? (comp not js/isNaN))

;; dis dont work
(defn is-integer?
  [s]
  (and (is-number? s) (integer? (js/parseInt s))))

(defn maps [f coll] (into #{} (map f coll)))
(defn mapm [f coll] (into {} (map f coll)))

(defn ok? [status] (= (keyword status) :ok))

(defn find-first [f coll] (first (filter f coll)))

(defn mapback
  [f coll]
  (into (empty coll) (map f coll)))

(defn fmap
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

(defn get-first-index
  [item coll]
  (first (keep-indexed #(when (= item %2) %1) coll)))

(defn get-after
  [v item]
  (let [index (get-first-index item v)]
    (if (nil? index)
      nil
      (let [next-index (inc index)]
        (if (= next-index (count v))
          (first v)
          (get v next-index))))))

(defn validate-property
  [{:keys [value type required validate] :as property}] 
  (assoc property :valid? (validate value)))

(defn validate-form
  [properties]
  (let [validated-properties (fmap validate-property properties)
        all-valid? (every? #(:valid? (val %)) validated-properties)]
    [validated-properties all-valid?]))
