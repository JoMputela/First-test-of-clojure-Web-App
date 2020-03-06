(ns guestbook.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :as form]
            [guestbook.models.db :as db]))



;; (defn home [] (layout/common
;; [:h1 "Guestbook"]
;; [:p "Welcome to my guestbook"] [:hr]
;; [:form
;; [:p "Name:"]
;; [:input]
;; [:p "Message:"]
;; [:textarea {:rows 10 :cols 40}]]))



;; (defn show-guests []
;;   [:ul.guests
;;    (for [{:keys [message name timestamp]}
;;          [{:message "Howdy" :name "Bob" :timestamp nil}
;;           {:message "Hello" :name "Bob" :timestamp nil}]]
;;      [:li
;;       [:blockquote message]
;;       [:p "-" [:cite name]]
;;       [:time timestamp]])])


(defn show-guests []
  [:ul.guests
   (for [{:keys [message name timestamp]} (db/read-guests)]
     [:li
      [:blockquote message]
      [:p "-" [:cite name]]
      [:time timestamp]])])



(defn home [& [name message error]]
  (layout/common
   [:h1 "My Super Allster Guestbook"]
   [:p "Welcome to my VIP guestbook"]
   [:p error]

;here we call our show-guests function
;to generate the list of existing comments

   (show-guests)
   [:hr]

;here we create a form with text fields called "name" and "message" ;these will be sent when the form posts to the server as keywords of ;the same name

   (form/form-to [:post "/"]
                 [:p "1-Name:"]
                 (form/text-field "name" name)
                 [:p "2-Message:"]
                 (form/text-area {:rows 10 :cols 40} "message" message)
                 [:br]
                 (form/submit-button "comment/print"))))


;; (defn save-message [name message]
;;   (cond
;;     (empty? name)
;;     (home name message "Some dummy forgot to leave a name - name")
;;     (empty? message)
;;     (home name message "Don't you have something to say? - comment area")
;;     :else
;;     (do
;;       (println name message)
;;       (home))))

(defn save-message [name message]
  (cond
    (empty? name)
    (home name message "Some dummy  forgot to leave a name")
    (empty? message)
    (home name message "Don't you have something to say?")
    :else
    (do
      (db/save-message name message)
      (home))))



(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message))

  )

(defn format-time [timestamp]
  (-> "dd/MM/yyyy"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))
(defn show-guests []
  [:ul.guests
   (for [{:keys [message name timestamp]} (db/read-guests)]
     [:li
      [:blockquote message]
      [:p "-" [:cite name]]
      [:time (format-time timestamp)]])])


