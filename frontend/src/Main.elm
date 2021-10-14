module Main exposing (main)

import Browser
import Browser.Navigation as Nav
import Element
import Html exposing (..)
import Http
import Url exposing (Protocol(..))



-- MAIN


main : Program () Model Msg
main =
    Browser.application
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        , onUrlChange = UrlChanged
        , onUrlRequest = LinkClicked
        }



-- MODEL


baseUrl : String
baseUrl =
    "http://localhost:8080/tournaments"


type alias Participant =
    { name : String }


type BracketType
    = ByeBracket Participant
    | SingleBracket Participant Participant
    | IntermediateBracket BracketType BracketType


type alias Bracket =
    { id : String
    , bracket : BracketType
    , winner : Maybe Participant
    }


type alias Tournament =
    { id : String
    , name : String
    , participants : List Participant
    , bracket : Bracket
    , playable : List Bracket
    }


type alias Model =
    String


init : () -> Url.Url -> Nav.Key -> ( Model, Cmd Msg )
init flags url key =
    ( ""
    , Http.get
        { url = baseUrl ++ "/health"
        , expect = Http.expectString GotHealth
        }
    )



-- UPDATE


type Msg
    = UrlChanged Url.Url
    | LinkClicked Browser.UrlRequest
    | GotHealth (Result Http.Error String)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        UrlChanged url ->
            ( model, Cmd.none )

        LinkClicked urlRequest ->
            ( model, Cmd.none )

        GotHealth result ->
            case result of
                Ok _ ->
                    ( model, Cmd.none )

                -- TODO error handling
                Err _ ->
                    ( model, Cmd.none )



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions _ =
    Sub.none



-- VIEW
-- TODO test data, remove


tournament : Tournament
tournament =
    let
        bob =
            { name = "Bob" }

        alice =
            { name = "Alice" }

        bracket =
            { id = "345", bracket = SingleBracket bob alice, winner = Nothing }
    in
    { id = "12343"
    , name = "Smash"
    , participants = [ bob, alice ]
    , bracket = bracket
    , playable = [ bracket ]
    }



-- TODO styling


view : Model -> Browser.Document Msg
view model =
    { title = "Utournament"
    , body =
        [ h1 [] [ text "UTournament" ]
        , tournamentsAsTable [ tournament ]
        ]
    }


tournamentsAsTable : List Tournament -> Html msg
tournamentsAsTable ts =
    Element.layout []
        (Element.table []
            { data = ts
            , columns =
                [ { header = Element.text "Is finished"
                  , width = Element.shrink
                  , view =
                        \t ->
                            if List.length t.playable == 0 then
                                Element.text "🏆"

                            else
                                Element.text "🎮"
                  }
                , { header = Element.text "Name"
                  , width = Element.shrink
                  , view =
                        \t ->
                            Element.text t.name
                  }
                , { header = Element.text "Nb participants"
                  , width = Element.shrink
                  , view =
                        \t ->
                            Element.text (String.fromInt (List.length t.participants))
                  }
                ]
            }
        )
