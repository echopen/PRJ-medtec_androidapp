<?php

namespace App\Http\Controllers;

use App\Scan;
use Illuminate\Http\Request;

class ApiController extends Controller
{
    public function getScans(){
    	$scans = Scan::all();
    	return json_encode($scans);
	}
}
