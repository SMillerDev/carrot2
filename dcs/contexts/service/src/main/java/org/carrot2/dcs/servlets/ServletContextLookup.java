/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * https://www.carrot2.org/carrot2.LICENSE
 */
package org.carrot2.dcs.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Objects;
import javax.servlet.ServletContext;
import org.carrot2.util.ResourceLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServletContextLookup implements ResourceLookup {
  private static Logger console = LoggerFactory.getLogger("console");

  private final ServletContext ctx;
  private final String path;

  ServletContextLookup(ServletContext servletContext, String path) {
    this.ctx = servletContext;
    this.path = Objects.requireNonNull(path);
  }

  @Override
  public InputStream open(String resource) throws IOException {
    String resourcePath = resourcePath(resource);
    InputStream is = ctx.getResourceAsStream(resourcePath);

    console.trace(
        "Opening servlet context resource: {}, {}",
        is != null ? pathOf(resource) : resourcePath,
        is != null ? " (found)" : " (not found)");

    if (is == null) {
      throw new IOException("Resource not found in context: " + resourcePath);
    }
    return is;
  }

  @Override
  public boolean exists(String resource) {
    try {
      return ctx.getResource(resourcePath(resource)) != null;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String pathOf(String resource) {
    try {
      return ctx.getResource(resourcePath(resource)).toExternalForm();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private String resourcePath(String resource) {
    return this.path + resource;
  }
}