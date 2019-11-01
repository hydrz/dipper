CREATE ALIAS IF NOT EXISTS H2GIS_SPATIAL FOR "org.h2gis.functions.factory.H2GISFunctions.load";

CALL H2GIS_SPATIAL();

CREATE TABLE IF NOT EXISTS district
(
    id       INT         NOT NULL,
    parentId INT         NOT NULL,
    cityCode INT         NOT NULL DEFAULT 0,
    adCode   INT         NOT NULL DEFAULT 0,
    name     VARCHAR(32) NOT NULL DEFAULT '',
    level    VARCHAR(10) NOT NULL DEFAULT '',
    center   GEOMETRY,
    polyline GEOMETRY,
    PRIMARY KEY (id)
);

CREATE INDEX ON district (polyline);