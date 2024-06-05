(ns financeiro.core
  (:require [clj-http.client :as client]))
 ; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   (println "Hello, World!"))


(defn opcao1 []
  (println "Opção 1 selecionada")
  (let [response (client/get "http://3001/saldo")]
    (println "Resposta da API para opção 1:" (:body response))))

(defn opcao2 []
    (let [response (client/post "http://3001/transacoes")]
     (println "Resposta da API para opção 1:" (:body response))))

(defn opcao3 []
  (let [response (client/get "http://3001/transacoes")]
    (println "Resposta da API para opção 1:" (:body response))))

(defn opcao4 []
  (let [response (client/get "http://3001/receitas")]
    (println "Resposta da API para opção 1:" (:body response))))

(defn opcao5 []
  (let [response (client/get "http://3001/despesas")]
    (println "Resposta da API para opção 1:" (:body response))))


(defn mostrar-menu []
  (println "\nMenu:")
  (println "1. Opção 1")
  (println "2. Opção 2")
  (println "3. Opção 3")
  (println "4. Opcao 4")
  (println "5. Opcao 5"))

(defn handle-escolha [escolha]
  (cond
    (= escolha "1") (opcao1)
    (= escolha "2") (opcao2)
    (= escolha "3") (opcao3)
    (= escolha "4") (opcao4)
    (= escolha "5") (opcao5)
    :else (println "Opção inválida. Tente novamente.")))

(defn -main [& args]
  (loop []
    (mostrar-menu)
    (let [escolha (read-line)]
      (handle-escolha escolha)
      (when (not= escolha "4")
        (recur)))))

;;(-main)
