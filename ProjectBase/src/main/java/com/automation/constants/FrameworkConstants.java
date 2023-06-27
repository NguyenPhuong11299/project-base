package com.automation.constants;

import com.automation.helpers.PropertiesHelpers;

public class FrameworkConstants {
    public static final String TARGET = PropertiesHelpers.getValue("TARGET");
    public static final String HEADLESS = PropertiesHelpers.getValue("HEADLESS");

    public static final int WAIT_EXPLICIT = Integer.parseInt(PropertiesHelpers.getValue("WAIT_EXPLICIT"));
    public static final int WAIT_PAGE_LOADED = Integer.parseInt(PropertiesHelpers.getValue("WAIT_PAGE_LOADED"));
    public static final int WAIT_SLEEP_STEP = Integer.parseInt(PropertiesHelpers.getValue("WAIT_SLEEP_STEP"));
    public static final String ACTIVE_PAGE_LOADED = PropertiesHelpers.getValue("ACTIVE_PAGE_LOADED");

    public static final String URL_CRM = PropertiesHelpers.getValue("URL_CRM");

    public static final String INFO_BASE_EXCEL_PATH = PropertiesHelpers.getValue("INFO_BASE_EXCEL_PATH");
    public static final String TEST_ORDER = PropertiesHelpers.getValue("TEST_ORDER");
    public static final String CREATE_ORDER_DESIGN_URL = PropertiesHelpers.getValue("CREATE_ORDER_DESIGN_URL");

    public static final String TEST_API_LIST_BASE_RESULT = PropertiesHelpers.getValue("TEST_API_LIST_BASE_RESULT");

    public static final String URL_PREFIX = PropertiesHelpers.getValue("URL_PREFIX");
}
