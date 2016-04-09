package casso.util;

import android.support.annotation.Nullable;
import android.util.Xml;
import casso.model.Artwork;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XmlUtil {

    private enum EVENT_SOURCE {
        EVENT_SET, SUBJECT_EVENT;
    }

    private static final String NAMESPACE = null;
    private static final String OAI_PMH = "OAI-PMH";
    private static final String GET_RECORD = "GetRecord";
    private static final String RECORD = "record";
    private static final String METADATA = "metadata";
    private static final String LIDO_LIDO = "lido:lido";

    private static final String LIDO_CATEGORY = "lido:category";

    private static final String LIDO_DESCRIPTIVE_METADATA = "lido:descriptiveMetadata";
    private static final String LIDO_OBJECT_CLASSIFICATION_WRAP = "lido:objectClassificationWrap";
    private static final String LIDO_OBJECT_IDENTIFICATION_WRAP = "lido:objectIdentificationWrap";
    private static final String LIDO_EVENT_WRAP = "lido:eventWrap";
    private static final String LIDO_EVENT_SET = "lido:eventSet";
    private static final String LIDO_DISPLAY_EVENT = "lido:displayEvent";
    private static final String LIDO_EVENT = "lido:event";
    private static final String LIDO_EVENT_TYPE = "lido:eventType";
    private static final String LIDO_EVENT_ACTOR = "lido:eventActor";
    private static final String LIDO_ACTOR_IN_ROLE = "lido:actorInRole";
    private static final String LIDO_ACTOR = "lido:actor";
    private static final String LIDO_NAME_ACTOR_SET = "lido:nameActorSet";
    private static final String LIDO_PREF = "lido:pref";
    private static final String LIDO_EVENT_DATE = "lido:eventDate";
    private static final String LIDO_DATE = "lido:date";
    private static final String LIDO_EARLIEST_DATE = "lido:earliestDate";
    private static final String LIDO_LATEST_DATE = "lido:latestDate";
    private static final String LIDO_EVENT_MATERIALS_TECH = "lido:eventMaterialsTech";
    private static final String LIDO_MATERIALS_TECH = "lido:materialsTech";
    private static final String LIDO_TERM_MATERIALS_TECH = "lido:termMaterialsTech";
    private static final String LIDO_OBJECT_RELATION_WRAP = "lido:objectRelationWrap";
    private static final String LIDO_SUBJECT_WRAP = "lido:subjectWrap";
    private static final String LIDO_SUBJECT_SET = "lido:subjectSet";
    private static final String LIDO_SUBJECT = "lido:subject";
    private static final String LIDO_SUBJECT_CONCEPT = "lido:subjectConcept";
    private static final String LIDO_SUBJECT_PLACE = "lido:subjectPlace";
    private static final String LIDO_DISPLAY_PLACE = "lido:displayPlace";
    private static final String LIDO_SUBJECT_ACTOR = "lido:subjectActor";
    private static final String LIDO_DISPLAY_ACTOR = "lido:displayActor";
    private static final String LIDO_SUBJECT_EVENT = "lido:subjectEvent";
    private static final String LIDO_OBJECT_WORK_TYPE_WRAP = "lido:objectWorkTypeWrap";
    private static final String LIDO_OBJECT_WORK_TYPE = "lido:objectWorkType";
    private static final String LIDO_CLASSIFICATION_WRAP = "lido:classificationWrap";
    private static final String LIDO_CLASSIFICATION = "lido:classification";
    private static final String LIDO_TITLE_WRAP = "lido:titleWrap";
    private static final String LIDO_TITLE_SET = "lido:titleSet";

    private static final String LIDO_ADMINISTRATIVE_METADATA = "lido:administrativeMetadata";
    private static final String LIDO_RESOURCE_WRAP = "lido:resourceWrap";
    private static final String LIDO_RESOURCE_SET = "lido:resourceSet";
    private static final String LIDO_RESOURCE_REPRESENTATION = "lido:resourceRepresentation";
    private static final String LIDO_LINK_RESOURCE = "lido:linkResource";

    private static final String LIDO_APPELLATION_VALUE = "lido:appellationValue";
    private static final String LIDO_CONCEPT_ID = "lido:conceptID";
    private static final String LIDO_TERM = "lido:term";
    private static final String LIDO_TYPE = "lido:type";

    private static final String CLASSIFICATION = "Classification";
    private static final String CURATORIAL_COMMENT = "Curatorial comment";
    private static final String GENRE = "Genre";
    private static final String LARGE = "large";
    private static final String OBJECT_NAME = "Object name";
    private static final String PREFERRED = "preferred";

    public static void parse(InputStream in, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readOAIPMH(parser, builder);
        } finally {
            in.close();
        }
    }

    private static void readOAIPMH(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, OAI_PMH);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(GET_RECORD)) {
                readGetRecord(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readGetRecord(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, GET_RECORD);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(RECORD)) {
                readRecord(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readRecord(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, RECORD);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(METADATA)) {
                readMetadata(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readMetadata (XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, METADATA);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_LIDO)) {
                readLidoLido(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoLido(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_LIDO);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_CATEGORY)) {
                readLidoCategory(parser, builder);
            } else if (name.equals(LIDO_DESCRIPTIVE_METADATA)) {
                readLidoDescriptiveMetadata(parser, builder);
            } else if (name.equals(LIDO_ADMINISTRATIVE_METADATA)) {
                readLidoAdministrativeMetadata(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoCategory(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_CATEGORY);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TERM)) {
                builder.setCategory(readTag(parser, LIDO_TERM));
            } else {
                skip(parser);
            }
        }
    }

    private static String readLidoConceptIDType(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_CONCEPT_ID);
        String lidoConceptIDType = parser.getAttributeValue(NAMESPACE, LIDO_TYPE);
        parser.next();
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, NAMESPACE, LIDO_CONCEPT_ID);
        return lidoConceptIDType;
    }

    private static String readTag(XmlPullParser parser, String tagName)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tagName);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tagName);
        return text;
    }

    private static void readLidoDescriptiveMetadata(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_DESCRIPTIVE_METADATA);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_OBJECT_CLASSIFICATION_WRAP)) {
                readLidoObjectClassificationWrap(parser, builder);
            } else if (name.equals(LIDO_OBJECT_IDENTIFICATION_WRAP)) {
                readLidoObjectIdentificationWrap(parser, builder);
            } else if (name.equals(LIDO_EVENT_WRAP)) {
                readLidoEventWrap(parser, builder);
            } else if (name.equals(LIDO_OBJECT_RELATION_WRAP)) {
                readLidoObjectRelationWrap(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoObjectClassificationWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_OBJECT_CLASSIFICATION_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_OBJECT_WORK_TYPE_WRAP)) {
                readLidoObjectWorkTypeWrap(parser, builder);
            } else if (name.equals(LIDO_CLASSIFICATION_WRAP)) {
                readLidoClassificationWrap(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoObjectWorkTypeWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_OBJECT_WORK_TYPE_WRAP);
        List<String> objectTypes = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_OBJECT_WORK_TYPE)) {
                parser.nextTag();
                String conceptIDType = readLidoConceptIDType(parser);
                parser.nextTag();
                String term = readTag(parser, LIDO_TERM);
                parser.nextTag();
                if (conceptIDType != null && conceptIDType.equals(OBJECT_NAME)) {
                    objectTypes.add(term);
                } else if (conceptIDType != null && conceptIDType.equals(GENRE)) {
                    builder.setGenre(term);
                }
            } else {
                skip(parser);
            }
        }
        builder.setObjectTypes(objectTypes.size() > 0 ? objectTypes : null);
    }

    private static void readLidoClassificationWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_CLASSIFICATION_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_CLASSIFICATION)) {
                parser.nextTag();
                String conceptIDType = readLidoConceptIDType(parser);
                parser.nextTag();
                String term = readTag(parser, LIDO_TERM);
                parser.nextTag();
                if (conceptIDType != null && conceptIDType.equals(CLASSIFICATION)) {
                    builder.setClassification(term);
                }
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoObjectIdentificationWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_OBJECT_IDENTIFICATION_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TITLE_WRAP)) {
                readLidoTitleWrap(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoTitleWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_TITLE_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TITLE_SET)) {
                readLidoTitleSet(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoTitleSet(XmlPullParser parser, Artwork.Builder builder)
        throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_TITLE_SET);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_APPELLATION_VALUE)) {
                builder.setTitle(readTag(parser, LIDO_APPELLATION_VALUE));
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoEventWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_EVENT_SET)) {
                readLidoEventSet(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoEventSet(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_SET);
        String lidoDisplayEvent = null;
        String curator = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_DISPLAY_EVENT)) {
                lidoDisplayEvent = readTag(parser, LIDO_DISPLAY_EVENT);
            } else if (name.equals(LIDO_EVENT)) {
                curator = readLidoEvent(parser, builder, EVENT_SOURCE.EVENT_SET);
            } else {
                skip(parser);
            }
        }
        if (curator != null && !curator.equals("") &&
                lidoDisplayEvent != null && !lidoDisplayEvent.equals("")) {
            builder.setCuratorialComment(lidoDisplayEvent);
            builder.setCurator(curator);
        }
    }

    /*
    If called from readLidoEventSet:
        If eventType is Curatorial comment, returns the curator's name.
        Else returns null.
    If called from readLidoSubjectEvent, returns the tag (eventType).
     */
    private static String readLidoEvent(
            XmlPullParser parser,
            @Nullable Artwork.Builder builder,
            EVENT_SOURCE eventSource)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT);
        String lidoEventType = null;
        String curator = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_EVENT_TYPE)) {
                lidoEventType = readLidoEventType(parser);
            } else if (name.equals(LIDO_EVENT_ACTOR)) {
                String actor = readLidoEventActor(parser);
                if (lidoEventType != null && lidoEventType.equals(CURATORIAL_COMMENT)) {
                    curator = actor;
                } else if (builder != null ) {
                    builder.setArtist(actor);
                }
            } else if (name.equals(LIDO_EVENT_DATE)) {
                readLidoEventDate(parser, builder);
            } else if (name.equals(LIDO_EVENT_MATERIALS_TECH)) {
                readLidoEventMaterialsTech(parser, builder);
            } else {
                skip(parser);
            }
        }
        if (eventSource == EVENT_SOURCE.EVENT_SET) {
            return curator;
        } else if (eventSource == EVENT_SOURCE.SUBJECT_EVENT) {
            return lidoEventType;
        } else {
            return null;
        }
    }

    private static String readLidoEventType(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_TYPE);
        String eventType = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TERM)) {
                eventType = readTag(parser, LIDO_TERM);
            } else {
                skip(parser);
            }
        }
        return eventType;
    }

    private static String readLidoEventActor(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_ACTOR);
        String actor = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_ACTOR_IN_ROLE)) {
                actor = readLidoActorInRole(parser);
            } else {
                skip(parser);
            }
        }
        return actor;
    }

    private static String readLidoActorInRole(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_ACTOR_IN_ROLE);
        String actor = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_ACTOR)) {
                actor = readLidoActor(parser);
            } else {
                skip(parser);
            }
        }
        return actor;
    }

    private static String readLidoActor(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_ACTOR);
        String actor = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_NAME_ACTOR_SET)) {
                actor = readLidoNameActorSet(parser);
            } else {
                skip(parser);
            }
        }
        return actor;
    }

    private static String readLidoNameActorSet(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_NAME_ACTOR_SET);
        String actor = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String lidoPref = parser.getAttributeValue(NAMESPACE, LIDO_PREF);
            if (name.equals(LIDO_APPELLATION_VALUE) && lidoPref != null &&
                    lidoPref.equals(PREFERRED)) {
                actor = readTag(parser, LIDO_APPELLATION_VALUE);
            } else {
                skip(parser);
            }
        }
        return actor;
    }

    private static void readLidoEventDate(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_DATE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_DATE)) {
                readLidoDate(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoDate(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_DATE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(LIDO_EARLIEST_DATE)) {
                builder.setStartYear(StringUtil.getIntegerOrNull(readTag(parser, LIDO_EARLIEST_DATE)));
            } else if (name.equals(LIDO_LATEST_DATE)) {
                builder.setEndYear(StringUtil.getIntegerOrNull(readTag(parser, LIDO_LATEST_DATE)));
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoEventMaterialsTech(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_EVENT_MATERIALS_TECH);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_MATERIALS_TECH)) {
                readLidoMaterialsTech(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoMaterialsTech(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_MATERIALS_TECH);
        List<String> materials = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TERM_MATERIALS_TECH)) {
                String material = readLidoTermMaterialsTech(parser);
                if (material != null) {
                    materials.add(material);
                }
            } else {
                skip(parser);
            }
        }
        builder.setMaterials(materials.size() > 0 ? materials : null);
    }

    private static String readLidoTermMaterialsTech(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_TERM_MATERIALS_TECH);
        String material = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TERM)) {
                material = readTag(parser, LIDO_TERM);
            } else {
                skip(parser);
            }
        }
        return material;
    }

    private static void readLidoObjectRelationWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_OBJECT_RELATION_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_SUBJECT_WRAP)) {
                readLidoSubjectWrap(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoSubjectWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_SUBJECT_SET)) {
                readLidoSubjectSet(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoSubjectSet(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_SET);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_SUBJECT)) {
                readLidoSubject(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoSubject(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        Set<String> tagsSet = new HashSet<>();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = null;
            String name = parser.getName();
            if (name.equals(LIDO_SUBJECT_CONCEPT)) {
                tag = readLidoSubjectConcept(parser);
            } else if (name.equals(LIDO_SUBJECT_PLACE)) {
                tag = readLidoSubjectPlace(parser);
            } else if (name.equals(LIDO_SUBJECT_ACTOR)) {
                tag = readLidoSubjectActor(parser);
            } else if (name.equals(LIDO_SUBJECT_EVENT)) {
                tag = readLidoSubjectEvent(parser);
            } else {
                skip(parser);
            }
            if (tag != null) {
                tagsSet.add(StringUtil.stripParentheses(tag));
            }
        }
        List<String> tagsList = new ArrayList<>();
        tagsList.addAll(tagsSet);
        Collections.sort(tagsList);
        builder.setTags(tagsList.size() > 0 ? tagsList : null);
    }

    private static String readLidoSubjectConcept(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_CONCEPT);
        String tag = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_TERM)) {
                tag = readTag(parser, LIDO_TERM);
            } else {
                skip(parser);
            }
        }
        return tag;
    }

    private static String readLidoSubjectPlace(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_PLACE);
        String tag = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_DISPLAY_PLACE)) {
                tag = readTag(parser, LIDO_DISPLAY_PLACE);
            } else {
                skip(parser);
            }
        }
        return tag;
    }

    private static String readLidoSubjectEvent(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_EVENT);
        String tag = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_EVENT)) {
                tag = readLidoEvent(parser, null, EVENT_SOURCE.SUBJECT_EVENT);
            } else {
                skip(parser);
            }
        }
        return tag;
    }

    private static String readLidoSubjectActor(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_SUBJECT_ACTOR);
        String tag = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_DISPLAY_ACTOR)) {
                tag = readTag(parser, LIDO_DISPLAY_ACTOR);
            } else {
                skip(parser);
            }
        }
        return tag;
    }

    private static void readLidoAdministrativeMetadata(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_ADMINISTRATIVE_METADATA);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_RESOURCE_WRAP)) {
                readLidoResourceWrap(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoResourceWrap(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_RESOURCE_WRAP);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_RESOURCE_SET)) {
                readLidoResourceSet(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoResourceSet(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_RESOURCE_SET);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String type = parser.getAttributeValue(NAMESPACE, LIDO_TYPE);
            if (name.equals(LIDO_RESOURCE_REPRESENTATION) && type.equals(LARGE)) {
                readLidoResourceRepresentation(parser, builder);
            } else {
                skip(parser);
            }
        }
    }

    private static void readLidoResourceRepresentation(XmlPullParser parser, Artwork.Builder builder)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIDO_RESOURCE_REPRESENTATION);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals(LIDO_LINK_RESOURCE)) {
                builder.setImageUrl(readTag(parser, LIDO_LINK_RESOURCE));
            } else {
                skip(parser);
            }
        }
    }

    private static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip (XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth -= 1;
                    break;
                case XmlPullParser.START_TAG:
                    depth += 1;
                    break;
            }
        }
    }

}
