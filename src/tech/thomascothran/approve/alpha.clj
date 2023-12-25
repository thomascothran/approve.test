(ns tech.thomascothran.approve.alpha
  (:require [clojure.test :refer [is]]
            [clojure.string :as str]
            [tech.thomascothran.approve.io :as io]))

(def ^:dynamic *approval-test-store*
  :file)

(def ^:dynamic *encoder*
  :edn/pretty-print)

(defn- test-vars->path
  [vars]
  (->> vars
       (map (comp #(subs % 2) str))
       (mapcat #(str/split % #"[\\./]"))))

(defn- ->path
  ([] (->path t/*testing-vars* t/*testing-contexts*))
  ([test-vars _test-context]
   (test-vars->path test-vars)))

(defn validate
  ([validator-name value]
   (validate validator-name value {:store *approval-test-store*
                                   :test-path (->path)
                                   :encoder *encoder*}))
  ([validator-name value {:keys [store test-path] :as m}]
   (assert keyword? validator-name)
   (let [opts (assoc m :validation-name (name validator-name))]
     (assert store)
     (assert test-path)
     (io/save-received! store value opts)
     (let [previous-value (io/get-approved store opts)]
       (is (= previous-value value
              "The previously received value should match the current value"))))))

(comment
  (str *ns*))
