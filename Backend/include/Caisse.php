<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 03/05/2018
 * Time: 23:32
 */

class Caisse
{

    private $_nb_client;
    private $_caissier;
    private $conn;

    /**
     * Caisse constructor.
     * @param $_nb_client
     * @param $_caissier
     */
    public function __construct()
    {

    }

    /**
     * @return mixed
     */
    public function getNbClient()
    {
        return $this->_nb_client;
    }

    /**
     * @return mixed
     */
    public function getCaissier()
    {
        return $this->_caissier;
    }






}