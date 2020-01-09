<?php

/**
 *Cette classe s'occupe principalement de la connexion de base de données.
 *
 */
class DbConnect {
    private $conn;//variable de la coonexion a la base

    function __construct() { //construicteur de la classe DbConnect
    }
    /**
     * établissement de la connexion
     * @return gestionnaire de connexion de base de données
     */
    function connect() {

        /**
         * Importation du fichier 'Config.php' contenant les parametre de connexion de la base.
         * 'dirname(__FILE__)" signifie : le dossier parent du fichier en cours.
         * Donc,ici on importe le fichier Config.php qui a le meme dossier parent que le fichier en cours
         */
        include_once dirname(__FILE__) . '/Config.php';


// Connexion à la base de données mysql
        $this->conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

// Vérifiez les erreurs de connexion à la base de données
        if (mysqli_connect_errno()) {
            echo "Impossible de se connecter à MySQL: " . mysqli_connect_error();
        }
//retourner la ressource de connexion
        return $this->conn;
    }
}