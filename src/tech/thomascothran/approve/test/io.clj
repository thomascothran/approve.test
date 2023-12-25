(ns tech.thomascothran.approve.test.io
  (:refer-clojure :exclude [get read])
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.edn :as edn])
  (:import [java.io File FileNotFoundException]))

(defn- path-seq->string
  [path-seq]
  (str/join File/separator path-seq))

(defmulti write!
  (fn [store _destination _x]
    store))

(defmethod write!
  :file
  [_store path string]
  (let [file (File. path)]
    (-> file (.getParentFile) (.mkdirs))
    (spit file string)))

(defmulti read
  (fn [store _path] store))

(defmethod read
  :file
  [_store path]
  (def rp path)
  (try (-> (slurp path)
           (edn/read-string))
       (catch FileNotFoundException _)))

(defmulti decode
  (fn [decoder x]
    [decoder (type x)]))

(defmethod decode
  [:edn/pretty-print String]
  [_ x]
  (edn/read-string x))

(defmulti encode
  (fn [encoder _x]
    encoder))

(defmethod encode
  :edn/pretty-print
  [_ value]
  (let [encoded-value (with-out-str (pp/pprint value))
        decoded-value (decode :edn/pretty-print encoded-value)]
    (when (not= value decoded-value)
      (throw (ex-info "Cannot round trip values"
                      {:value value
                       :encoded-value encoded-value
                       :decoded-value decoded-value})))
    encoded-value))

(comment
  (encode :edn/pretty-print {:a 1}))

(comment
  (let [e (encode :edn/pretty-print {:a 1})
        d (decode :edn/pretty-print e)]
    d))

(defmulti encoding-format->file-extension
  identity)

(defmethod encoding-format->file-extension
  :edn/pretty-print
  [_] "edn")

(defmulti make-path
  (fn [store _opts]
    store))

(defmethod make-path
  :file
  [_store {:keys [encoder input-type test-path validation-name root]
           :or {root ["test-resources" "approve"]}}]
  (let [extension-type (encoding-format->file-extension encoder)
        input-type (case input-type :received "received" :approved "approved")
        filename (str validation-name "." input-type "." extension-type)
        path-seq (reduce into root [test-path [filename]])]
    (path-seq->string path-seq)))

(comment
  (make-path :file {:approval-test-name "test"
                    :input-type :approved
                    :validation-name "validation"
                    :encoding-format :edn/pretty-print}))

(defn- -save!
  [store value {:keys [encoder]
                :or   {encoder :edn/pretty-print}
                :as opts}]
  (let [encoded-value (encode encoder value)
        path (make-path store (assoc opts :encoder encoder))]
    (write! store path encoded-value)))

(defn save-approved!
  [store value opts]
  (-save! store value (assoc opts :input-type :approved)))

(defn save-received!
  [store value opts]
  (-save! store value (assoc opts :input-type :received)))

(defn- get
  [store {:keys [encoder]
          :or   {encoder :edn/pretty-print}
          :as opts}]
  (let [path (make-path store (assoc opts :encoder encoder))]
    (read store path)))

(defn get-approved
  [store {:keys [encoder]
          :or {encoder :edn/pretty-print}
          :as opts}]
  (get store (assoc opts
                    :input-type :approved
                    :encoder encoder)))

(defn get-received
  [store {:keys [encoder]
          :or {encoder :edn/pretty-print}
          :as opts}]
  (get store (assoc opts
                    :input-type :received
                    :encoder encoder)))

(comment
  (let [opts {:approval-test-name "test"
              :validation-name "validation"}
        _ (save-approved! :file {:a 1} opts)]
    (get-approved :file opts)))

(comment
  (let [opts {:approval-test-name "testing-again"
              :validation-name "validation"}
        #_#__ (save-received! :file {:a 1} opts)]
    (get-received :file opts)))

