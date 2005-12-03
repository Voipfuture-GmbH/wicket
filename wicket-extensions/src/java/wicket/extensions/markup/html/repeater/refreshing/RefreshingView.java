package wicket.extensions.markup.html.repeater.refreshing;

import java.util.Iterator;

import wicket.extensions.markup.html.repeater.OrderedRepeatingView;
import wicket.model.IModel;
import wicket.version.undo.Change;

/**
 * An abstract repeater view that provides refreshing functionality to its
 * subclasses. Items are refreshed every request, making this view well suited
 * for displaying dynamic data.
 * <p>
 * The view is populated by overriding the <code>getItemModels()</code> method
 * and providing an iterator that returns models for items to be added to the
 * view. RefreshingView builds the items that will be rendered by looping over
 * the models and calling the
 * <code>newItem(String id, int index, IModel model)</code> to generate the
 * child item container followed by <code>populateItem(Component item)</code>
 * to let the user populate the newly created item container with custom
 * components.
 * </p>
 * <p>
 * The provided {@link ModelIteratorAdapter} can make implementing
 * {@link RefreshingView#getItemModels() } easier if you have an iterator over
 * item objects.
 * </p>
 * 
 * @see wicket.extensions.markup.html.repeater.OrderedRepeatingView
 * @see ModelIteratorAdapter
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class RefreshingView extends OrderedRepeatingView
{
	private static final long serialVersionUID = 1L;

	/**
	 * The item reuse strategy that will be used to recycle items when the page
	 * is changed or the view is redrawn.
	 * 
	 * @see IItemReuseStrategy
	 */
	private IItemReuseStrategy itemReuseStrategy;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 */
	public RefreshingView(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model
	 */
	public RefreshingView(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Refresh the items in the view. Delegates the creation of items to the
	 * selected item reuse strategy
	 */
	protected void internalOnBeginRequest()
	{
		super.internalOnBeginRequest();

		if (isVisibleInHierarchy())
		{

			IItemFactory itemFactory = new IItemFactory()
			{

				public Item newItem(int index, IModel model)
				{
					String id = RefreshingView.this.newChildId();
					Item item = RefreshingView.this.newItem(id, index, model);
					RefreshingView.this.populateItem(item);
					return item;
				}

			};

			Iterator models = getItemModels();
			Iterator items = getItemReuseStrategy().getItems(itemFactory, models, getItems());
			removeAll();
			addItems(items);
		}

	}

	/**
	 * Returns an iterator over models for items that will be added to this view
	 * 
	 * @return an iterator over models for items that will be added to this view
	 */
	protected abstract Iterator getItemModels();

	/**
	 * Populate the given Item container.
	 * <p>
	 * <b>be carefull</b> to add any components to the item and not the view
	 * itself. So, don't do:
	 * 
	 * <pre>
	 * add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * but:
	 * 
	 * <pre>
	 * item.add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param item
	 *            The item to populate
	 */
	protected abstract void populateItem(final Item item);

	/**
	 * Factory method for Item container. Item containers are simple
	 * MarkupContainer used to aggregate the user added components for a row
	 * inside the view.
	 * 
	 * @see Item
	 * @param id
	 *            component id for the new data item
	 * @param index
	 *            the index of the new data item
	 * @param model
	 *            the model for the new data item
	 * 
	 * @return DataItem created DataItem
	 */
	protected Item newItem(final String id, int index, final IModel model)
	{
		return new Item(id, index, model);
	}

	/**
	 * @return iterator over item instances that exist as children of this view
	 */
	public Iterator getItems()
	{
		return iterator();
	}

	/**
	 * Add items to the view. Prior to this all items were removed so every
	 * request this function starts from a clean slate.
	 * 
	 * @param items
	 *            item instances to be added to this view
	 */
	protected void addItems(Iterator items)
	{
		while (items.hasNext())
		{
			add((Item)items.next());
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// ITEM GENERATION
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * @return currently set item reuse strategy. Defaults to
	 *         <code>DefaultItemReuseStrategy</code> if none was set.
	 * 
	 * @see DefaultItemReuseStrategy
	 */
	public IItemReuseStrategy getItemReuseStrategy()
	{
		if (itemReuseStrategy == null)
		{
			return DefaultItemReuseStrategy.getInstance();
		}
		return itemReuseStrategy;
	}

	/**
	 * Sets the item reuse strategy.
	 * 
	 * @param strategy
	 *            item reuse strategy
	 */
	public void setItemReuseStrategy(IItemReuseStrategy strategy)
	{
		if (strategy == null)
		{
			throw new IllegalArgumentException();
		}

		if (!strategy.equals(itemReuseStrategy))
		{
			addStateChange(new Change()
			{
				private static final long serialVersionUID = 1L;

				private final IItemReuseStrategy old = itemReuseStrategy;

				public void undo()
				{
					itemReuseStrategy = old;
				}

				public String toString()
				{
					return "ItemsReuseStrategyChange[component: " + getPath() + ", reuse: " + old
							+ "]";
				}
			});
		}
		itemReuseStrategy = strategy;
	}


}
