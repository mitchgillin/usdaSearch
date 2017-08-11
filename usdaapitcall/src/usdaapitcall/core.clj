(ns usdaapitcall.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
))


(def myAPIKEY (slurp "usdaAPIKEY.txt"))
(def myBaseURL " https://api.nal.usda.gov/ndb/search/?format=json")


(defn buildURL
  [base & args]
  (apply str base args)

  )


(defn itemName
  [item]
  (clojure.string/join ["&q=" item]
                       )
  )

(defn totalNumberOfItems
  [num]
  (clojure.string/join ["&total=" num]))

(defn apiKey
  [key]
  (clojure.string/join ["&api_key=" key]))

(defn baseURL
  [url]
  (clojure.string/join [url]))




(defn -main
  [item]
  (let [response (client/get (buildURL (baseURL myBaseURL) (itemName item) (apiKey myAPIKEY)) )]
    (generate-string response)
 )
)

(defn jsonBody
  [item]
  "Given the JSON response, strip it down to just the body contianing the name of the returned items"
  (->(-main item) (parse-string true) (get :body))
  )

(defn nextName
  "taking in the body, return the names recursevly until it reaches the end.
  Based on the USDA naming listing convention of having Name + 8 starting the name of the item, and ending it with a ,"
  [bodystring nameIndex]

  (println(subs bodystring (+ (clojure.string/index-of bodystring "name" nameIndex) 8 nameIndex) (+ (clojure.string/index-of bodystring ","(clojure.string/index-of bodystring "name")) nameIndex))

  )
)
(defn allNames
  [item]

  (loop [item item index 0]

  (nextName (jsonBody item) index)
  (recur item (+ index 2))
  )
)
