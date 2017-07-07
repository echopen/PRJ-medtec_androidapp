<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/


Route::prefix('api')->group(function () {
	Route::get('users', 'ApiController@getUsers');
	Route::get('users/connect', 'ApiController@connectUser');
	Route::get('users/register', 'ApiController@registerUser');
	Route::get('users/{id}', 'ApiController@getUser');
	Route::get('scans', 'ApiController@getScans');
	Route::get('scans/public', 'ApiController@getPublicScans');
	Route::get('scans/{id}', 'ApiController@getScan');
});

Route::get('/', 'PagesController@home');
Route::get('/contact', 'PagesController@contact');


Route::get('/community', 'PagesController@community');


Route::get('/product', 'PagesController@product');

Route::get('/faq', 'PagesController@faq');

Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');
