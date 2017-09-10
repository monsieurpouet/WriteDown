<?php

require_once 'Parsedown.php';
require_once 'dbconnect.php';

$id_note= $_GET['id'];

$query_id = "SELECT * FROM note WHERE id=$id_note";
$result = $connect->query($query_id);
$Parsedown = new Parsedown();

if($query_id == true)
        {

        while ($donnees = $result->fetch_assoc()){

?>

    <p>

    ID :<?php echo $donnees['id']; ?><br />
    Titre : <?php echo $donnees['titre']; ?><br />
    Content : <?php echo $Parsedown->text($donnees['content']); ?><br />
Date de Creation : <?php echo $donnees['create_date']; ?><br />
Date de dernière modification : <?php echo $donnees['modify_date']; ?><br />
   
</p>

<?php
                }

        }

$connect->close();



?>
