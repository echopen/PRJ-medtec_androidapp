<?php

use Illuminate\Database\Seeder;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
		DB::table('users')->insert([
			'id' => 1,
			'name' => "Pascal Montaz",
			'email' => "pascal.montaz@gmail.com",
			'password' => bcrypt('hello123'),
		]);
    }
}
