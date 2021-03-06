/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.restassured.specification;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import org.apache.http.client.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * A request specification that also supports getting the defined values. Intended for Filters.
 */
public interface FilterableRequestSpecification extends RequestSpecification {

    /**
     * @return The base URI defined in the request specification
     */
    String getBaseUri();

    /**
     * @return The base path defined in the request specification
     */
    String getBasePath();

    /**
     * @return The derived request path with path parameters and base path etc applied
     */
    String getDerivedPath();

    /**
     * Returns the original request path as it was before any path parameters were applied. For example
     * if you made the following request to REST Assured:
     * <pre>
     * get("/something/{x}", x);
     * </pre>
     * <p/>
     * Then this method would return <code>"/something/{x}"</code>.
     *
     * @return The original request path
     */
    String getUserDefinedPath();

    /**
     * @return The request method of the request (E.g. POST, GET etc)
     */
    String getMethod();

    /**
     * @return The request URI as a string. This is the fully-qualified path including host, port number, scheme, path and query params.
     */
    String getURI();

    /**
     * @return The port defined in the request specification
     */
    int getPort();

    /**
     * @return The request content type defined in the request specification
     */
    String getContentType();

    /**
     * @return The authentication scheme defined in the request specification
     */
    AuthenticationScheme getAuthenticationScheme();

    /**
     * @return The request parameters defined in the request specification
     */
    Map<String, String> getRequestParams();

    /**
     * @return The form parameters defined in the request specification
     */
    Map<String, String> getFormParams();

    /**
     * @return The all path parameters defined in the request specification (both named and unnamed)
     */
    Map<String, String> getPathParams();

    /**
     * @return The named path parameters defined in the request specification
     */
    Map<String, String> getNamedPathParams();

    /**
     * Return the unnamed path parameters defined in the request specification as a map.
     * Note that this only works when the unnamed path parameters are balanced (meaning that a place holder was defined in the user defined path and
     * a value exists for this placeholder as an unnamed path parameter). For example when the request is defined as:
     * <p/>
     * <pre>
     * get("/{x}/{y}", "one", "two");
     * </pre>
     * then this method will return <code>{ "x" : "one, "y" : "two" }</code>. But if the request is missing the an unnamed path param for "y":
     * <pre>
     * get("/{x}/{y}", "one");
     * </pre>
     * then this method will return <code>{ "x" : "one" }</code>.
     * If the request is defined like this:
     * <pre>
     * get("/{x}/{y}", "one", "two", "three");
     * </pre>
     * then this method will return <code>{ "x" : "one, "y" : "two" }</code>.
     * <p/>
     * If all you want is a list of the supplied unnamed path parameter (values) use {@link #getUnnamedPathParamValues()}.
     *
     * @return The unnamed path parameters defined in the request specification.
     */
    Map<String, String> getUnnamedPathParams();

    /**
     * @return A list of all unnamed path parameters supplied to the request
     * @see #getUnnamedPathParams()
     */
    List<String> getUnnamedPathParamValues();

    /**
     * @return The query parameters defined in the request specification
     */
    Map<String, String> getQueryParams();

    /**
     * @return The multipart segments defined in the request specification
     */
    List<MultiPartSpecification> getMultiPartParams();

    /**
     * @return The headers defined in the request specification
     */
    Headers getHeaders();

    /**
     * @return The cookies defined in the request specification
     */
    Cookies getCookies();

    /**
     * @return The request body
     */
    <T> T getBody();

    /**
     * @return The filters (unmodifiable)
     */
    List<Filter> getDefinedFilters();

    /**
     * @return The Rest Assured configurations
     */
    RestAssuredConfig getConfig();

    /**
     * @return The underlying http client. Only use this for advanced configuration which is not accessible from Rest Assured! By default an instance of {@link org.apache.http.impl.client.AbstractHttpClient} is used by REST Assured.
     */
    HttpClient getHttpClient();

    /**
     * @return The defined proxy specification or <code>null</code> if undefined.
     */
    ProxySpecification getProxySpecification();

    /**
     * Set the request path of the request specification. For example if the request was defined like this:
     * <p/>
     * <pre>
     * get("/x");
     * </pre>
     * <p/>
     * You can change to path to "/y" instead using this method. This will result in a request that looks like this:
     * <p/>
     * <pre>
     * get("/y");
     * </pre>
     *
     * @param path The path
     * @return the filterable request specification
     */
    FilterableRequestSpecification path(String path);

    /**
     * Returns a list of all path param placeholders that are currently undefined in the request. For example if consider the following request:
     * <p/>
     * <pre>
     * get("/{x}/{y}");
     * </pre>
     * <p/>
     * Calling <code>getPathParamPlaceholder()</code> will return a list with "x" and "y". Note that if you have a path like this:
     * <pre>
     * get("/{x}/{x}");
     * </pre>
     * <p/>
     * the list will include "x" twice. Also note that this function will only return those placeholders that are not yet defined.
     * I.e. calling this method when the request is defined like this:
     * <pre>
     * get("/{x}/{y}", "something");
     * </pre>
     * will only return a list of "y". Use {@link #getPathParamPlaceholders()} to get ALL placeholders.
     *
     * @return A list of all path param templates that were defined in the request
     * @see #getPathParamPlaceholders()
     */
    List<String> getUndefinedPathParamPlaceholders();

    /**
     * Returns a list of all path param placeholders that are currently undefined in the request. For example if consider the following request:
     * <p/>
     * <pre>
     * get("/{x}/{y}");
     * </pre>
     * <p/>
     * Calling <code>getPathParamPlaceholders()</code> will return a list with "x" and "y". Note that if you have a path like this:
     * <pre>
     * get("/{x}/{x}");
     * </pre>
     * <p/>
     * the list will include "x" twice. Note that this function will return all placeholders as they were when the user issued the request.
     * I.e. calling this method when the request is defined like this:
     * <pre>
     * get("/{x}/{y}", "something");
     * </pre>
     * will return a list of "x" and "y". Use {@link #getUndefinedPathParamPlaceholders()} to get a list of only the placeholders that are
     * currently undefined ("y" in this case).
     *
     * @return A list of all path param templates that were defined in the request
     * @see #getUndefinedPathParamPlaceholders()
     */
    List<String> getPathParamPlaceholders();

    /**
     * Remove a form parameter from the request.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeFormParam(String parameterName);

    /**
     * Remove a path parameter from the request. It will remove both named and unnamed path parameters.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removePathParam(String parameterName);

    /**
     * Remove a named path parameter from the request. It will remove both named and unnamed path parameters.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeNamedPathParam(String parameterName);

    /**
     * Remove an unnamed path parameter from the request.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeUnnamedPathParam(String parameterName);

    /**
     * Remove the first unnamed path parameter from the request based on its value.
     *
     * @param parameterValue The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeUnnamedPathParamByValue(String parameterValue);

    /**
     * Remove a request parameter from the request.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeParam(String parameterName);

    /**
     * Remove a query parameter from the request.
     *
     * @param parameterName The parameter key
     * @return The {@link FilterableRequestSpecification} without the parameter
     */
    FilterableRequestSpecification removeQueryParam(String parameterName);

    /**
     * Remove a header with the given name.
     *
     * @param headerName The header name
     * @return The {@link FilterableRequestSpecification} without the specified header
     */
    FilterableRequestSpecification removeHeader(String headerName);

    /**
     * Remove a cookie with the given name.
     *
     * @param cookieName The cookie name
     * @return The {@link FilterableRequestSpecification} without the specified cookie
     */
    FilterableRequestSpecification removeCookie(String cookieName);

    /**
     * Remove a cookie
     *
     * @param cookie The cookie
     * @return The {@link FilterableRequestSpecification} without the specified cookie
     */
    FilterableRequestSpecification removeCookie(Cookie cookie);

    /**
     * Replace a header with the new value. If the headerName doesn't exist the will be added.
     *
     * @param headerName The header name to replace
     * @return The {@link FilterableRequestSpecification} with the replaced header
     */
    FilterableRequestSpecification replaceHeader(String headerName, String newValue);

    /**
     * Replace a cookie with the given name. If the cookieName doesn't exist it will be added.
     *
     * @param cookieName The cookie name
     * @return The {@link FilterableRequestSpecification} with the replaced cookie
     */
    FilterableRequestSpecification replaceCookie(String cookieName, String value);

    /**
     * Replace a cookie, if it doesn't exist then it will be added.
     *
     * @param cookie The cookie
     * @return The {@link FilterableRequestSpecification} with the replaced cookie
     */
    FilterableRequestSpecification replaceCookie(Cookie cookie);

    /**
     * Replace all defined headers
     *
     * @param headers The new headers
     * @return The {@link FilterableRequestSpecification} with the replaced headers
     */
    FilterableRequestSpecification replaceHeaders(Headers headers);


    /**
     * Replace all defined cookies
     *
     * @param cookies The new cookies
     * @return The {@link FilterableRequestSpecification} with the replaced cookies
     */
    FilterableRequestSpecification replaceCookies(Cookies cookies);

    /**
     * Removes all defined headers
     *
     * @return The {@link FilterableRequestSpecification} without
     */
    FilterableRequestSpecification removeHeaders();


    /**
     * Removed all defined cookies
     *
     * @return The {@link FilterableRequestSpecification} with the replaced cookies
     */
    FilterableRequestSpecification removeCookies();
}