<?php

namespace App\Http\Controllers;

use App\Scan;
use App\User;
use Illuminate\Http\Request;

class ApiController extends Controller
{
    public function getScans(){
    	$scans = Scan::all();
    	return json_encode($scans);
	}

	public function getScan($id){
    	$scan = Scan::where('id', $id)
				->first();
    	return json_encode($scan);
	}

	public function getUsers(){
		$users = User::all()->take(100);
		return json_encode($users);
	}

	public function getUser($user_id){
		$user = User::where('users.id', $user_id)
			->first();
		$scans = User::select('scans.*')->
		join('scans', 'scans.user_id', '=', 'users.id')
			->where('users.id', $user_id)
			->get();

		$data = [];

		$data['user'] = $user;
		$data['scans'] = $scans;
		return json_encode($data);
	}

	public function getPublicScans(){
		$scans = Scan::where('public', 1)->first();
		return json_encode($scans);
	}
}
