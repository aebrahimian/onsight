<?php

set_time_limit(0);
date_default_timezone_set('UTC');

require __DIR__.'/vendor/autoload.php';

if ($argc < 5) {
	echo "too few argument. username, passsword, photo path & caption is needed.\n";
    exit(255);
}

/////// CONFIG ///////
$username = $argv[1];
$password = $argv[2];
$debug = true;
$truncatedDebug = false;
//////////////////////

/////// MEDIA ////////
$photoFilename = $argv[3];
$captionText = $argv[4];
//////////////////////

echo 'working directory '.getcwd().' filename '.$argv[0].' username '.$username.' pass '.$password.' photo path '.$photoFilename.' caption '.$captionText."\n";

$ig = new \InstagramAPI\Instagram($debug, $truncatedDebug);

try {
    $ig->setUser($username, $password);
    $ig->login();
} catch (\Exception $e) {
    echo 'Something went wrong: '.$e->getMessage()."\n";
    exit(254);
}

try {
    $ig->uploadTimelinePhoto($photoFilename, ['caption' => $captionText]);
    exit(1);
} catch (\Exception $e) {
    echo 'Something went wrong: '.$e->getMessage()."\n";
 	exit(254);   
}