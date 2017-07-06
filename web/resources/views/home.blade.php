@extends('layout.app')

@section('content')
    <section class="welcome">
        @include('components.home_presentation')
    </section>
 
    @include('components.home_nl')

    @include('components.home_history')

    @include('components.home.community')
  
    @include('components.home_nl')

    @include('components.home_faq')
    
    @include('components.home_community')
    <section class="base_section base_section-numbers">
        @include('components.home_numbers')
    </section>

    <section class="base_section base_section-faq">
       @include('components.home_faq')
    </section>
    
    
    <section class="base_section base_section-community">
       @include('components.home_community')
    </section>

     <section class="base_section base_section-howItWorks">
       @include('components.home_howItWorks')
    </section>


    @include('components.home.partners')

@endsection
