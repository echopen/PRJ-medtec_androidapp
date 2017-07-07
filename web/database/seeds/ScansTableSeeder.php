<?php

use Illuminate\Database\Seeder;

class ScansTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
		DB::table('scans')->insert([
			'id' => 1,
			'user_id' => 1,
			'img_src' => "scans/scan_01.png",
			'title' => "Scan n°37016",
			'comment' => "Prise de sang effectuée pour Gérard Bora",
			'public' => 1,
		]);
		DB::table('scans')->insert([
			'id' => 2,
			'user_id' => 1,
			'img_src' => "scans/scan_02.png",
			'title' => "Scan n°37017",
			'comment' => "Prise de sang effectuée pour Brigitte Carsol",
			'public' => 0,
		]);
    }
}
