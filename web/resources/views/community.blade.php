@extends('layout.app')

@section('content')
    @if(Auth::guest())
        @include('components.community.guest')
    @else
        @include('components.community.logged')
    @endif
@endsection
