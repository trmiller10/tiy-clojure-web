(ns tiy-clojure-map-filter.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defonce server (atom nil))

;(c/defroutes app
;             (c/GET "/" []
;               (h/html [:html
;                        [:body
;                         [:h2 "Hello, world!"]]])))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty app {:port 3000 :join? false})))


(defn read-purchases []
  ;(println "Enter a category (Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry:")
  (let [purchases (slurp "purchases.csv")

        ;splits purchases into vectors of lines
        purchases (str/split-lines purchases)
        purchases (map (fn [line] (clojure.string/split line #",")) purchases)
        headers (map keyword (first purchases))
        purchases (rest purchases)
        purchases (map (fn [line] (zipmap headers line)) purchases)]
        purchases))

(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (if (= 0 (count category))
                    purchases
                    (filter (fn [purchase]
                              (= (get purchase :category) category))
                            purchases))]

    [:ol
     (map (fn [purchase]
            [:li (str
                   (get purchase :customer_id)
                   " "
                   (get purchase :date)
                   " "
                   (get purchase :date)
                   " "
                   (get purchase :credit_card)
                   " "
                   (get purchase :cvv)
                   " "
                   (get purchase :category))]) purchases)]))

(c/defroutes app
             (c/GET "/:category{.*}" [category]
               (h/html [:html
                        [:body
                         [:a {:href "http://localhost:3000/"} "All data"]
                         [:br]
                         [:a {:href "http://localhost:3000/Furniture"} "Furniture"]
                         [:br]
                         [:a {:href "http://localhost:3000/Alcohol"} "Alcohol"]
                         [:br]
                         [:a {:href "http://localhost:3000/Toiletries"} "Toiletries"]
                         [:br]
                         [:a {:href "http://localhost:3000/Shoes"} "Shoes"]
                         [:br]
                         [:a {:href "http://localhost:3000/Food"} "Food"]
                         [:br]
                         [:a {:href "http://localhost:3000/Jewelry"} "Jewelry"]
                         [:br]
                         (purchases-html category)]])))

;(c/defroutes app (c/GET "/:category{.*}" [category]
;                       (h/html [:html
;
;                                [:body
;                                [:a {:href "http://theironyard.com"} "The iron yard"]
;                               [:br]
;                              (purchases-html category)]])))

        ;category (read-line)
        ;purchases (filter (fn [line] (= (get line :category) category)) purchases)
        ;file-text (pr-str purchases)]
    ;(spit (str "filtered_purchases.edn") file-text)))

;this creates a function that displays the purchases list in html in a numbered list
;(defn purchases-html [category]
 ; (let [purchases (read-purchases)
   ;     purchases (if (= 0 (count category))
  ;               purchases ; show all purchases if the category is a blank string
    ;             (filter (fn [purchase]
     ;                      (= (get purchase "category") category))
      ;                   purchases))]
   ; [:ol
    ; (map (fn [purchase]                      ; " " (get purchase "customer_id")
     ;       [:li (str (get purchase "category"))])
      ;    purchases)]))


;filter the purchases based on category by the category being requested, rebind to purchases


;define route app
;creates a GET endpoint called "/:category" that takes category as an argument
;creates an html page that you can build out with vectors


;(c/defroutes app (c/GET "/:category{.*}" [category]
 ;                       (h/html [:html
;
 ;                                [:body
  ;                                [:a {:href "http://theironyard.com"} "The iron yard"]
   ;                               [:br]
    ;                              (purchases-html category)]])))

;create an atom that lets the jetty server be manipulated while it's being run (instead of having to restart
; the damn thing every time I want to test some changes to the code)
;(defonce server (atom nil))

;create a new main function with no arguments
;(defn -main []
  ;designates the server as somet
 ; (when @server (.stop @server))
  ;executes app function
  ;configurations
  ;port you listen on
  ;do not join into j's thread (will lock up repl),
  ; continues running jetty in background so that commands can be entered
  ;(reset! server (j/run-jetty app {:port 3000 :join? false})))