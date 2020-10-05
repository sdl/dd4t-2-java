package org.dd4t.test.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.dd4t.databind.viewmodel.base.TridionViewModelBase;

/**
 * dd4t-parent
 *
 * @author R. Kempees
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractModelClass extends TridionViewModelBase {

}
