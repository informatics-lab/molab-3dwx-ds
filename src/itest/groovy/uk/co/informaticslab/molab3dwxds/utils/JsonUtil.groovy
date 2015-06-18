package uk.co.informaticslab.molab3dwxds.utils

import org.json.simple.parser.ContainerFactory
import org.json.simple.parser.JSONParser

class JsonUtil {

    static toJsonObj(json) {

        ContainerFactory containerFactory = new ContainerFactory() {

            def List creatArrayContainer() {
                return new LinkedList();
            }

            def Map createObjectContainer() {
                return new LinkedHashMap();
            }
        }

        JSONParser parser = new JSONParser();
        return parser.parse(json, containerFactory)
    }
}
