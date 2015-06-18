package uk.co.informaticslab.molab3dwxds.api.controllers

import spock.lang.Specification
import uk.co.informaticslab.molab3dwxds.spock.extensions.httpclient.HttpClient

import static uk.co.informaticslab.molab3dwxds.utils.JsonUtil.toJsonObj

class RootControllerITest extends Specification {

    @HttpClient
    def client

    def "Http GET: /molab-3dwx-ds/"() {

        when:
        def resp = client.get(path: "/molab-3dwx-ds/")

        then:
        resp.status == 200
        resp.data == expected_response
    }

    static expected_response = toJsonObj('''
    {
      "_links" : {
        "self" : {
          "href" : "http://localhost:9000/molab-3dwx-ds/"
        },
        "media" : [ {
          "href" : "http://localhost:9000/molab-3dwx-ds/media"
        } ],
        "models" : [ {
          "href" : "http://localhost:9000/molab-3dwx-ds/models"
        } ]
      }
    }
    ''')
}
