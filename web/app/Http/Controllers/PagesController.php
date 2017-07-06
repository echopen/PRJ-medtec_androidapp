<?php

namespace App\Http\Controllers;

use App\Scan;
use Illuminate\Http\Request;

class PagesController extends Controller
{
    public function home(){
		return view('home');
	}


	public function contact(){
		return view('contact');
	}


	public function faq(){
		return view('faq');
	}

	public function community(){
		return view('community');
	}


	public function product(){
		return view('product');
	}

}
