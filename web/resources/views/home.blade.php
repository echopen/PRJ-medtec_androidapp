@extends('layout.app')

@section('content')
    <section class="welcome">
        @include('components.home_presentation')
    </section>
 
    @include('components.home_nl')

    @include('components.home_history')
  
    @include('components.home_nl')
    
    @include('components.home_faq')
    
    @include('components.home_community')
    
    
@endsection
