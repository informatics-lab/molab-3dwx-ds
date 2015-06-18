package uk.co.informaticslab.molab3dwxds.api.controllers

import spock.lang.Specification
import uk.co.informaticslab.molab3dwxds.spock.extensions.httpclient.HttpClient
import uk.co.informaticslab.molab3dwxds.spock.extensions.mongodb.MongoDBCollection

import static uk.co.informaticslab.molab3dwxds.utils.JsonUtil.toJsonObj
import static uk.co.informaticslab.molab3dwxds.utils.MongoUtil.date

class ModelsControllerITest extends Specification {

    @HttpClient
    def client

    @MongoDBCollection(database = "molab", collection = "images")
    def image_store

    @MongoDBCollection(database = "molab", collection = "videos")
    def video_store

    def "Http GET: /molab-3dwx-ds/models/" () {

        given:
        image_store.contains(image_data)

        when:
        def resp = client.get(path: "/molab-3dwx-ds/models/")

        then:
        resp.status == 200
        resp.data == expected_response
    }

    static image_data = [
        forecast_reference_time: date("2015-01-01T00:00:00.000Z"),
        forecast_time: date("2015-01-01T00:00:00.000Z"),
        phenomenon: "cloud_fraction_in_a_layer",
        model: "uk_v",
        resolution: [
            x: 4096,
            y: 4096
        ],
        data_dimensions: [
            x: 632,
            y: 812,
            z: 70
        ],
        geographic_region: [
            [
                lat: 10.0d,
                lng: 10.0d
            ],
            [
                lat: 10.0d,
                lng: 10.0d
            ],
            [
                lat: 10.0d,
                lng: 10.0d
            ],
            [
                lat: 10.0d,
                lng: 10.0d
            ]
        ]
    ]

    static expected_response = toJsonObj('''
    {
      "_links" : {
        "self" : {
          "href" : "http://localhost:9000/molab-3dwx-ds/models"
        },
        "uk_v" : [{
          "href" : "http://localhost:9000/molab-3dwx-ds/models/uk_v"
        }]
      }
    }
    ''')
}