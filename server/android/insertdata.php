<?php

//Insertion des données
//id int, titre text, content text, create_date text, modify_date text

require_once('dbconnect.php');

if($_SERVER['REQUEST_METHOD']=='POST')
{
        $id = $_POST['id'];
        $titre = $_POST['titre'];
	$content = $_POST['content'];
	$create_date = $_POST['create_date'];
	$modify_date = $_POST['modify_date'];

	$query_delete = "DELETE FROM note";
	$query_check = "SELECT * FROM note WHERE id=$id";
	$query_insert = "INSERT INTO note (`id`, `titre`, `content`, `create_date`, `modify_date`) VALUES ('$id','$titre','$content','$create_date','$modify_date')";
	$query_update = "UPDATE note SET id='$id', titre='$titre', content='$content', create_date='$create_date', modify_date='$modify_date' WHERE id='$id'";

	$result_check = $connect->query($query_check);

	if($result_check->num_rows > 0)
	{
		echo $query_update;
		$connect->query($query_update) or die ("Erreur de la mise à jour de la note");
	}
	else
	{
		echo $query_insert;
		$connect->query($query_insert) or die ("Erreur lors de l'insertion de la note");
	}

	
	//echo $query_check;
	//echo $query_update;
	//echo $query_insert;

        if(($query_check ==true) or ($query_update ==true) or ($query_insert == true))
	{
        	echo "Successfully Registered";
        }
	else
	{
        	echo $id." Not Registered";
		echo $connect->error;
        }
}
else
{
        echo "error POST";
}

//mysqli_close($connect);
$connect->close();

?>
