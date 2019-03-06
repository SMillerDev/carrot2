
/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * http://www.carrot2.org/carrot2.LICENSE
 */

package org.carrot2.text.preprocessing;

import org.carrot2.TestBase;
import org.carrot2.language.LanguageComponents;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.carrot2.text.preprocessing.PreprocessingContextAssert.assertThat;

/**
 *
 */
public class EnglishStemmingPreprocessingTest extends TestBase {
  PreprocessingContextBuilder contextBuilder;

  @Before
  public void prepareContextBuilder() {
    contextBuilder = new PreprocessingContextBuilder(LanguageComponents.load("English"));
  }

  @Test
  public void testLowerCaseWords() {
    PreprocessingContextAssert a = assertThat(new PreprocessingContextBuilder()
        .newDoc("data mining", "data mining")
        .buildContext(new BasicPreprocessingPipeline()));

    a.constainsStem("data").withTf(2).withDocumentTf(0, 2).withFieldIndices(0, 1);
    a.constainsStem("mine").withTf(2).withDocumentTf(0, 2).withFieldIndices(0, 1);
    assertThat(a.context.allStems.image.length).isEqualTo(2);

    assertThat(a.tokens().stream().map(t -> t.getStemImage()))
        .containsExactly("data", "mine", null,
            "data", "mine", null);
  }

  @Test
  public void testUpperCaseWords() {
    PreprocessingContextAssert a = assertThat(new PreprocessingContextBuilder()
        .newDoc("DATA MINING", "DATA MINING")
        .buildContext(new BasicPreprocessingPipeline()));

    a.constainsStem("data").withTf(2).withDocumentTf(0, 2).withFieldIndices(0, 1);
    a.constainsStem("mine").withTf(2).withDocumentTf(0, 2).withFieldIndices(0, 1);
    assertThat(a.context.allStems.image.length).isEqualTo(2);

    assertThat(a.tokens().stream().map(t -> t.getStemImage()))
        .containsExactly("data", "mine", null,
            "data", "mine", null);
  }

  @Test
  public void testMixedCaseWords() {
    PreprocessingContextAssert a = assertThat(new PreprocessingContextBuilder()
        .newDoc("DATA MINING Data Mining", "Data Mining Data Mining")
        .buildContext(new BasicPreprocessingPipeline()));

    a.constainsStem("data").withTf(4).withDocumentTf(0, 4).withFieldIndices(0, 1);
    a.constainsStem("mine").withTf(4).withDocumentTf(0, 4).withFieldIndices(0, 1);
    assertThat(a.context.allStems.image.length).isEqualTo(2);

    assertThat(a.tokens().stream().map(t -> t.getStemImage()))
        .containsExactly("data", "mine", "data", "mine", null,
            "data", "mine", "data", "mine", null);
  }
}
