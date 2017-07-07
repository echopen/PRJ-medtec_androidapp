<?php

namespace App\Http\Controllers;

use App\Scan;
use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

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


	public function connectUser(Request $request){
		if(isset($request['email']) && isset($request['password'])) {
			$user = User::where('email', $request['email'])->first();

			if(!empty($user->email)){
				if (Auth::attempt(['email' => $request['email'], 'password' => $request['password']])) {
					$data = [];
					$data['error'] = false;
					$data['user'] = $user;
					$scans = User::select('scans.*')->
					join('scans', 'scans.user_id', '=', 'users.id')
						->where('users.id', $user->id)
						->get();
					$data['scans'] = $scans;

					return json_encode($data);
				}
				else{
					return json_encode([
						'error' => true,
						"errorMessage" => "Bad password"
					]);
				}
			}
			else{
				return json_encode([
					'error' => true,
					"errorMessage" => "User doesn't exists"
				]);
			}
		}
		else{
			return json_encode([
				'error' => true,
				"errorMessage" => "Email or Password is empty"
			]);
		}
	}

	public function registerUser(Request $request){
		$email = $request['email'];
		$name = $request['name'];
		$password = $request['password'];

		if(isset($email) && isset($name) && isset($password)){
			if(empty(User::where('email', $email)->first())){
				$user = new User;
				$user->name = $name;
				$user->email = $email;
				$user->password = bcrypt($password);
				$user->save();

				$data['error'] = false;
				$data['user'] = $user;
				return json_encode($data);
			}
			else{
				$data['error'] = true;
				$data['errorMessage'] = "Email already exists";
				return json_encode($data);
			}
		}
		else{
			$data['error'] = true;
			$data['errorMessage'] = "Email or name or password is missing";
			return json_encode($data);
		}
	}
}
