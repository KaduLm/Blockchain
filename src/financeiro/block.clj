(ns financeiro.block
  (:require [clojure.data.json :as json]
            [financeiro.utils :refer [formatar-bloco]]
            [clojure.string :as str]))


(defn sha256 [data]
  (let [message-digest (java.security.MessageDigest/getInstance "SHA-256")]
    (->> (.digest message-digest (.getBytes data))
         (map #(format "%02x" %))
         (apply str))))

(defn novo-bloco [numero nonce dados hash-anterior]
  (let [dados-str (json/write-str dados :escape-slash false)]
    {:numero numero
     :nonce nonce
     :dados dados-str
     :hash-anterior hash-anterior
     :hash (sha256 (str numero nonce dados-str hash-anterior))}))


(defn bloco-valido? [bloco]
  (= (subs (:hash bloco) 0 4) "0000"))

(defn encontrar-nonce [numero dados hash-anterior]
  (loop [nonce 0]
    (let [bloco (novo-bloco numero nonce dados hash-anterior)]
      (if (bloco-valido? bloco)
        nonce
        (recur (inc nonce))))))

(def blockchain (atom [{:numero 0
                        :nonce 0
                        :dados []
                        :hash-anterior "00000000000000000000000000000000"
                        :hash "0000"}]))

(defn exibir-blockchain []
  (doseq [bloco @blockchain]
    (println (formatar-bloco bloco))
    (println)))

(defn adicionar-bloco [transacao valor]
  (swap! blockchain
         (fn [chain]
           (let [ultimo-bloco (last chain)
                 numero (inc (:numero ultimo-bloco))
                 hash-anterior (:hash ultimo-bloco)
                 dados {:transacao transacao :valor valor}
                 nonce (encontrar-nonce numero dados hash-anterior)
                 novo (novo-bloco numero nonce dados hash-anterior)]
             (conj chain novo)))))

(defn adicionar-transacao [transacao valor]
  (adicionar-bloco transacao valor))

(defn -main []
  (println "Insira os detalhes da transação:")
  (print "Transação: ")
  (flush)
  (let [transacao (read-line)]
    (print "Valor: ")
    (flush)
    (let [valor (read-line)]
      (adicionar-bloco transacao (Integer/parseInt valor))
      (println "Blockchain atualizado:")
      (doseq [bloco @blockchain]
        (println (formatar-bloco bloco))
        (println)))) ; Adiciona uma linha em branco entre os blocos
  )

(defn -main [& args]
  (if (= (count args) 2)  ; Verifica se há exatamente 2 argumentos passados
    (let [transacao (nth args 0)
          valor (Integer/parseInt (nth args 1))]
      (adicionar-bloco transacao valor)
      (doseq [bloco @blockchain]
        (println (formatar-bloco bloco))))
    (println "Uso: block.clj <transacao> <valor>")))

(-main)